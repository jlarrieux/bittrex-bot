package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Comparators;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Log
@Data
@Component
public class SideWaysTrader  extends AbstractTrader{


    private double stopLoss, profitTaking, spreadThreshold, rsiOverBought, rsiOverSold, rsiNoMomentum, tradingMinimum, currentBTCbalance;


    private OrderManager orderManager;
    private int orderTimeOutInMinutes;



    @Autowired
    public SideWaysTrader( TradingProperties properties, PositionManager positionManager, OrderManager orderManager){
        super(properties,positionManager,orderManager);

    }

    @Override
    public void executeStrategy() {

        Collections.sort(potentialMarkets, new Comparators.MarketRsiComparatorAscending());
        for(Market m: potentialMarkets){
//            makeBuy();
        }
    }



    private void evaluate(Market m){

    }



    private double calculateBuyAmount(Market market){
        double ask = market.getAsk();
        return tradingMinimum/ask;
    }














    @Override
    public void makeBuy(Market market) {
        if(okToBuy(market)) orderManager.initiateBuy(market.getMarketName());

    }






    @Override
    protected boolean okToBuy(Market market) {
        double rsi = market.getCurrentRSI();
        double bollingerHigh = market.getBollingerHigh();
        double currentPrice = market.getLast();
        if(rsi<=rsiOverSold && currentPrice<bollingerHigh) return true;
        else return false;
    }



    @Override
    public void makeSell(Market market) {
        if(okToSell(market))     orderManager.initiateSell(market.getMarketName());

    }



    @Override
    protected boolean okToSell(Market market) {
        double rsi = market.getCurrentRSI();
        double bollingerLow = market.getBollingerLow();
        double currentPrice = market.getLast();
        double pAndL = orderManager.getPandL(market);
        log.info(String.format("SELL INDICATOR: currency: %s  currentRSI: %s\t\t currentPrice: %s  "
                , Constants.addSpace(market.getMarketCurrency()), Constants.addSpaceForDouble(rsi,2,""),Constants.addSpaceForDouble(currentPrice, Constants.CURRENCY_PRECISION, Constants.BTC)) );

        if( (rsi>= rsiOverBought  && currentPrice>bollingerLow) || pAndL<= stopLoss || pAndL>= profitTaking) return true;
        else     return false;
    }





}
