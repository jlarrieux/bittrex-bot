package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.Entity.Orders;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.OrderAdapater;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;



@Log
@Component
@Data
public class OrderManager {


    private Orders orderBooks;

//    private HashMap<String,Order> initiatedOrders = new HashMap<>();
    private HashMap<String, Order> pendingBuyOrderTracker = new HashMap<>();
    private HashMap<String, Order> pendingSellOrderTracker = new HashMap<>();
    private PositionManager positionManager ;
    private double buyIncrement;
    private int orderTimeOutInMinutes;



    OrderAdapater orderAdapater;

    @Autowired
    public OrderManager( Orders orders, PositionManager positionManager, TradingProperties properties, OrderAdapater orderAdapater){
        this.orderAdapater = orderAdapater;
        buyIncrement = properties.getMinimumBtc();
        this.orderBooks = orders;
        this.positionManager = positionManager;
        this.orderTimeOutInMinutes = properties.getOrderTimeOutInMinutes();
        getOpenOrders();
    }




    public void getOpenOrders(){
        log.info("Getting open orders...");
        iterateJsonArrayOrder(orderAdapater.getAllOpenOrders());

    }


    private void iterateJsonArrayOrder(JsonArray array){
        orderBooks.clear();
        Order order;
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

    public void initiateSell(String currency){
        double quantity = positionManager.getQuantityOwn(currency);
        double unitPrice = getSellPrice(currency);
        if (quantity>0){
            Order order = orderAdapater.sell(currency,quantity,unitPrice);//getOrder( client.sell(currency,quantity,unitPrice))  ord;
            if(order!= null) pendingSellOrderTracker.put(order.getOrderUuid(), order);
        }


    }

    public String initiateBuy(String marketName){
        double unitPrice =-1;//todo calculate true unit price
        return trueBuying(marketName, unitPrice,0);

    }

    public String initiateBuy(String marketName,double quantity, double price){
//        log.info(String.format("Initiate buy: "));
        return trueBuying(marketName,quantity, price);
    }

    private String trueBuying(String marketName,  double quantity,double unitPrice){
        if(quantity==-1) quantity= buyIncrement/unitPrice;
        StringBuilder uuid = null;
        log.info(String.format("marketname: %s\tquantity: %f\tunitPrice: %f", marketName, quantity,unitPrice));
        Order order = orderAdapater.buy(marketName,quantity,unitPrice);
        if(order!=null){
            uuid = new StringBuilder();
            pendingBuyOrderTracker.put(order.getOrderUuid(), order);
            uuid.append(order.getOrderUuid());
        }
        return uuid.toString();
    }









    public Double getPandL(Market market) {
        if(positionManager.contains(market.getMarketCurrency())){
            double total = positionManager.getTotalPricePaid(market.getMarketCurrency());
            double quantity = positionManager.getQuantityOwn(market.getMarketCurrency());
        }

        //todo implement
        return null;
    }


    private double getSellPrice(String marketName){

        return 0;
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
                pendingSellOrderTracker.remove(key);
            }
            else if(cancelBecauseOfTimeTooOld(remote.getOpened())){
                if(partialUpdateHasOccurred(localOrder, remote)) updateForPartialExecution(localOrder,remote);
                orderAdapater.cancelOrder(remote.getOrderUuid());
            }
            else if (partialUpdateHasOccurred(localOrder,remote));//todo implement negative update


        }
    }



    private boolean partialUpdateHasOccurred(Order local, Order remote){
        boolean hasPariallyExecuted = remote.getQuantity() != remote.getQuantityRemaining();
        boolean localIsUnaware = remote.getQuantityRemaining() != local.getQuantityRemaining();
        return localIsUnaware && hasPariallyExecuted;

    }


    private void updateForPartialExecution(Order localOrder, Order remote){
            localOrder.setQuantityRemaining(remote.getQuantityRemaining());
            double diff = localOrder.getQuantity() - localOrder.getQuantityRemaining();
            localOrder.setQuantity(remote.getQuantityRemaining());
            if(localOrder.getType()== Order.orderType.LIMIT_SELL);
            Position p = new Position(diff, localOrder.getLimit(), "");//todo get market currency
            positionManager.add(p);
    }




    private boolean cancelBecauseOfTimeTooOld(LocalDateTime orderUtcTime){
        LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC());
        localDateTime.minusMinutes(orderTimeOutInMinutes);

        return localDateTime.isBefore(orderUtcTime);
    }




    private Position createPositionFromOrder( Order localOrder){
        Position p = null;
        String s = "";//todo get market currency
        if(s!=null) p = new Position(localOrder, s);

        return  p;

    }








}
