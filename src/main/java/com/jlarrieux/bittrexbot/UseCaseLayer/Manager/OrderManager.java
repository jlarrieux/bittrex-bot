package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.Entity.Orders;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketOrderBookAdapater;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.OrderAdapater;
import com.jlarrieux.bittrexbot.Util.Analytics;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;


@Log
@Component
@Data
public class OrderManager {


    private Orders orderBooks;
    private ConcurrentHashMap<String, Order> pendingBuyOrderTracker = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Order> pendingSellOrderTracker = new ConcurrentHashMap<>();
    private PositionManager positionManager ;
    private double buyIncrement;
    private int orderTimeOutInMinutes;
    private MarketSummaryAdapter marketSummaryAdapter;

    @Autowired
    private Analytics analytics;


    MarketOrderBookAdapater marketOrderBookAdapater;
    OrderAdapater orderAdapater;

    @Autowired
    public OrderManager( Orders orders, PositionManager positionManager, TradingProperties properties, OrderAdapater orderAdapater, MarketOrderBookAdapater marketOrderBookAdapater, MarketSummaryAdapter marketSummaryAdapter){
        this.orderAdapater = orderAdapater;
        buyIncrement = properties.getMinimumBtc();
        this.orderBooks = orders;
        this.positionManager = positionManager;
        this.orderTimeOutInMinutes = properties.getOrderTimeOutInMinutes();
        this.marketOrderBookAdapater = marketOrderBookAdapater;
        this.marketSummaryAdapter = marketSummaryAdapter;
        //getOpenOrders();
    }








    public void getOpenOrders(){
        log.info("Getting open orders...");
        iterateJsonArrayOrder(orderAdapater.getAllOpenOrders());

    }


    private void iterateJsonArrayOrder(JsonArray array){
        orderBooks.clear();

        for(int i =0; i<array.size(); i++) orderBooks.add(array,i);

    }






    public void cancelOldOrders(LocalDateTime cutoff){
        getOpenOrders();
//        log.info("orderbook size:"+String.valueOf(orderBooks.size()));
        for(String key: orderBooks.keySet()){
            Order o = orderBooks.get(key);
            boolean before = o.getOpened().isBefore(cutoff);
//            log.info(String.format("is it before? %b\t order date: %s \t\t cutoffdate: %s", before, o.getOpened().toString(), cutoff.toString()));
            if(before) orderAdapater.cancelOrder(o.getOrderUuid());//client.cancelOrder(o.getOrderUuid());
        }
    }

    public String initiateSell(String marketName, String currency){


        marketOrderBookAdapater.executeMarketOrderBook(marketName);
        double quantity = positionManager.getQuantityOwn(currency);
        double unitPrice = marketOrderBookAdapater.getFirstSellPrice();
        StringBuilder uuid = new StringBuilder();
        uuid.append( actualSell(marketName,quantity,unitPrice));
        if(uuid.toString().length()>0) positionManager.remove(currency);
        return uuid.toString();
    }


    public String actualSell(String marketName, double quantity, double unitPrice){
        StringBuilder uuid = null;
        Order order = orderAdapater.sell(marketName, quantity, unitPrice);
        log.info(String.format("\n\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n\nTRUE_SELL \tcurrency: %s\tquantity: %f\tunitPrice: %f\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n\n\n", marketName, quantity,unitPrice));
        if(order!=null){
            uuid = new StringBuilder();
            pendingSellOrderTracker.put(order.getOrderUuid(), order);
            uuid.append(order.getOrderUuid());
        }

        checkPendingOrders();//todo Simulation only! needs to be removed for production run
        analytics.addTransaction(Analytics.OrderType.SELL,marketName, quantity, unitPrice);

        StringBuilder builder = new StringBuilder();
        if(uuid!=null) builder.append(uuid.toString());
        return builder.toString();

    }

    public String initiateBuy(String marketName){
        marketOrderBookAdapater.executeMarketOrderBook(marketName);
        double unitPrice = marketOrderBookAdapater.getFirstBuyPrice();
        double quantity = marketOrderBookAdapater.getFirstBuyQuantity();
        double fallBackQuantity = buyIncrement/unitPrice;
        if(fallBackQuantity< quantity) quantity = fallBackQuantity;
        return actualBuying(marketName, quantity,unitPrice);

    }




    public String initiateBuy(String marketName,double quantity, double price){
//        log.info(String.format("Initiate buy: "));
        return actualBuying(marketName,quantity, price);
    }

    private String actualBuying(String marketName, double quantity, double unitPrice){
        if(quantity==-1) quantity= buyIncrement/unitPrice;
        StringBuilder uuid = null;
        log.info(String.format("\n\n\n!!!!!!!!!!!!!!!!!!!!!!!!\n\n" +
                "TRUE_BUY \tmarketname: %s\tquantity: %f\tunitPrice: %f\n!!!!!!!!!!!!!!!!!!!!!\n\n\n",
                marketName, quantity,unitPrice));
        Order order = orderAdapater.buy(marketName,quantity,unitPrice);
        if(order!=null){
            uuid = new StringBuilder();
            pendingBuyOrderTracker.put(order.getOrderUuid(), order);
            uuid.append(order.getOrderUuid());
        }

        checkPendingOrders();//todo Simulation only! needs to be removed for production run
        analytics.addTransaction(Analytics.OrderType.BUY,marketName, quantity, unitPrice);

        StringBuilder builder = new StringBuilder();
        if(uuid!=null) builder.append(uuid.toString());
        return builder.toString();
    }









    public Double getPandL(Market market) {
        if(positionManager.contains(market.getMarketCurrency())){
            double totalPriceAtBuy = positionManager.getTotalPricePaid(market.getMarketCurrency());
            double quantity = positionManager.getQuantityOwn(market.getMarketCurrency());
            double currentValueOfQuantity = quantity* market.getLast();
            return currentValueOfQuantity- totalPriceAtBuy;
        }

        return null;
    }


    private double getSellPrice(String marketName){
        marketOrderBookAdapater.executeMarketOrderBook(marketName);
        return marketOrderBookAdapater.getFirstBuyPrice();
    }

    //@Scheduled(fixedRate = 1500) -- should be bring back on when running in prod
    public void checkPendingOrders(){
        decideOnPendingBuyOrders();
        decideOnPendingSellOrders();
    }


    private void decideOnPendingBuyOrders(){

        for(String uuid: pendingBuyOrderTracker.keySet()){
            Order localOrder = pendingBuyOrderTracker.get(uuid);
            Order remote = orderAdapater.getOrder(localOrder.getOrderUuid());//new Order(JsonParserUtil.getJsonObjectFromJsonString(responseFromOrderUUID.getResult()));
            if (!remote.getIsOpen() && !remote.getCancelIniated()){

                    Position p = createPositionFromOrder(localOrder);
                    if(p!=null) {
                        positionManager.add(p);
                        pendingBuyOrderTracker.remove(uuid);
                    }
            }
            else if (cancelBecauseOfTimeTooOld(remote.getOpened())) {
                if(partialUpdateHasOccurred(localOrder,remote)) updateForPartialExecution(localOrder,remote);
                orderAdapater.cancelOrder(remote.getOrderUuid());
            }

            else if(partialUpdateHasOccurred(localOrder,remote))   updateForPartialExecution(localOrder,remote);

        }
    }


    private void decideOnPendingSellOrders(){
        for (String key: pendingSellOrderTracker.keySet()){
            Order localOrder = pendingSellOrderTracker.get(key);

            Order remote = orderAdapater.getOrder(localOrder.getOrderUuid());
            if(!remote.getIsOpen() && ! remote.getCancelIniated()){
                positionManager.remove(key);

            }
            else if(cancelBecauseOfTimeTooOld(remote.getOpened())){
                if(partialUpdateHasOccurred(localOrder, remote)) updateForPartialExecution(localOrder,remote);
                orderAdapater.cancelOrder(remote.getOrderUuid());
                positionManager.add(createPositionFromOrder(remote));
            }
            else if (partialUpdateHasOccurred(localOrder,remote)) updateForPartialExecution(localOrder,remote);//todo implement negative update


        }
    }



    private boolean partialUpdateHasOccurred(Order local, Order remote){
        boolean hasPartiallyExecuted = remote.getQuantity() != remote.getQuantityRemaining();
        boolean localIsUnaware = remote.getQuantityRemaining() != local.getQuantityRemaining();
        return localIsUnaware && hasPartiallyExecuted;

    }


    private void updateForPartialExecution(Order localOrder, Order remote){
            localOrder.setQuantityRemaining(remote.getQuantityRemaining());
            double diff = localOrder.getQuantity() - localOrder.getQuantityRemaining();
            localOrder.setQuantity(remote.getQuantityRemaining());
            if(localOrder.getType()== Order.orderType.LIMIT_SELL);
            Position p = new Position(diff, localOrder.getLimit(), marketSummaryAdapter.getMarketCurrencyShort(localOrder.getMarketName()));
            positionManager.add(p);
    }




    private boolean cancelBecauseOfTimeTooOld(LocalDateTime orderUtcTime){
        LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC());
        localDateTime.minusMinutes(orderTimeOutInMinutes);

        return localDateTime.isBefore(orderUtcTime);
    }




    private Position createPositionFromOrder( Order localOrder){
        Position p = null;
        StringBuilder shortCurrency =new StringBuilder( marketSummaryAdapter.getMarketCurrencyShort(localOrder.getMarketName()));
        StringBuilder longCurrency = new StringBuilder(marketSummaryAdapter.getMarketCurrencyLong(localOrder.getMarketName()));
        if(shortCurrency!=null && longCurrency!=null){
            p = new Position(localOrder, shortCurrency.toString(), longCurrency.toString());
        }

        return  p;

    }








}
