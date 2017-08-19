package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@Log
@Data
@Component
public class Trader {


    private double stopLoss, profitTaking, spreadThreshold, rsiOverBought, rsiOverSold, rsiNoMomentum, tradingMinimum, currentBTCbalance;
    private PositionManager positionManager;
    private MyBittrexClient client;
    private OrderManager orderManager;
    private int orderTimeOutInMinutes;


    @Autowired
    public Trader(MyBittrexClient client, TradingProperties tradingProperties, PositionManager positionManager, OrderManager orderManager){
        stopLoss = tradingProperties.getStopLoss();
        profitTaking = tradingProperties.getProfitTaking();
        spreadThreshold = tradingProperties.getSpread();
        rsiOverBought = tradingProperties.getRsiOverBought();
        rsiOverSold = tradingProperties.getRsiOverSold();
        tradingMinimum = tradingProperties.getMinimumBtc();
        rsiNoMomentum = tradingProperties.getRsiNoMomentum();
        this.positionManager = positionManager;
        this.client = client;
        this.orderManager = orderManager;
        this.orderTimeOutInMinutes = tradingProperties.getOrderTimeOutInMinutes();
    }


    public void makeTrade(List<Market> potentialBuys, List<Market> potentialSells){
        currentBTCbalance = getBalance();
        cancelOldOrders();
//        makeSells(potentialSells);
//        makeBuys(potentialBuys);
    }




    private void makeBuys(List<Market> potentialBuys){
        for(int i=0; i<potentialBuys.size();i++){
            Market market = potentialBuys.get(i);
            double quantity = calculateBuyAmount(market);
            if(buyDecision(market)) {
                double price =market.getAsk();
                double amount = positionManager.buy(market,quantity, price);
                log.info(String.format("+++++++++BUYING: %s, with quantity %f and total amount %f with currentRSI: %f and  and bid: %f and ask: %f and last: %f and price: %f\n%s\n\n", market.getMarketCurrency(), quantity, amount, market.getCurrentRSI(),market.getBid(), market.getAsk(), market.getLast(), price, market.bollingerToString()));
            }
        }
    }


    private boolean buyDecision(Market market){
        boolean aboveMinTrade = currentBTCbalance >market.getMinTradeSize();
        boolean enoughForTrade = currentBTCbalance > positionManager.calculateTotal(market.getAsk()*calculateBuyAmount(market));
        return buyBecauseOfIndicator(market) && aboveMinTrade && enoughForTrade;
    }


    private boolean buyBecauseOfIndicator(Market market){
        boolean rsi = market.getCurrentRSI()<rsiOverSold;
        double currentPrice = market.getLast();
        double b = market.getBollingerEMA().getHigh();
        boolean bol = currentPrice<b;
        log.info(String.format("BUY INDICATOR: currency: %s   currentRSI: %.2f\t\t   currentPrice: %s    EMAbollingerHighThreshold: %s  "
                , Constants.addSpace(market.getMarketCurrency()), market.getCurrentRSI(),Constants.addSpaceForDouble(market.getLast(), Constants.CURRENCY_PRECISION, Constants.BTC),Constants.addSpaceForDouble(market.getBollingerEMA().getBHighThreshold(), Constants.CURRENCY_PRECISION,"")));
        return rsi && bol;
    }

    private double calculateBuyAmount(Market market){
        double ask = market.getAsk();
        return tradingMinimum/ask;
    }

    private void makeSells(List<Market> potentialSells){
        log.info("#Of potential sells: "+ potentialSells.size());
        for(Market m: potentialSells){
            if(sellDecision(m)){
                Position p = positionManager.getPositionBooks().get(m.getMarketName());
                double amount = positionManager.sell(p);
                currentBTCbalance+= amount;
                log.info(String.format("-------SELLING: %s, with quantity %f and total amount %f\n\n", m.getMarketCurrency(), p.getQuantity(), amount));
            }
        }
    }


    private boolean sellDecision(Market market){
        Position p = positionManager.getPositionBooks().get(market.getMarketName());
        double purchasePrice = p.getLastPrice();
        double pAndL = (purchasePrice - market.getLast())/purchasePrice;
        boolean profit = pAndL > profitTaking;
        boolean loss = pAndL< stopLoss;

        return sellBecauseOfIndicator(market) || profit || loss;
    }

    private boolean sellBecauseOfIndicator(Market market){
        boolean rsi = market.getCurrentRSI()> rsiOverBought;
        double currentPrice = market.getLast();
        double b = market.getBollingerEMA().getLow();
        boolean bol = currentPrice>b;
        log.info(String.format("SELL INDICATOR: currency: %s  currentRSI: %s\t\t currentPrice: %s EMAbollingerLowThreshold: %s  "
                , Constants.addSpace(market.getMarketCurrency()), Constants.addSpaceForDouble(market.getCurrentRSI(),2,""),Constants.addSpaceForDouble(market.getLast(), Constants.CURRENCY_PRECISION, Constants.BTC), Constants.addSpaceForDouble(market.getBollingerEMA().getBLowThreshold(), Constants.CURRENCY_PRECISION,"") ));
        return rsi && bol;

    }



    private Double getBalance(){
        Response response = new Response(client.getBalance("BTC").toString());
        Double result = null;
        if(response.isSuccess()){
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
            result= JsonParserUtil.getDoubleFromJsonObject(object, "Available");
        }
        return  result;
    }


    public void cleanSlate(){
        orderManager.cancelAll();
        positionManager.sellAll();

    }

    public void cancelOldOrders(){
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(orderTimeOutInMinutes);
        ZonedDateTime zonedDateTime = cutoff.atZone(ZoneId.systemDefault());
        ZonedDateTime utcCutOff = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        orderManager.cancelOldOrders(utcCutOff.toLocalDateTime());

    }

    public void cleanUp(){
        List<Position> allPositions = positionManager.getAllOpenPositions();
        if(allPositions!=null){
            for(Position p: allPositions){
                if(!positionManager.contains(p)) positionManager.sell(p);
            }
        }
    }






}
