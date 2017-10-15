package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Comparators;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
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


    private double  rsiOverBought, rsiOverSold, rsiNoMomentum;




    @Autowired
    public SideWaysTrader(TradingProperties properties, OrderManager orderManager){
        super(properties,orderManager );

    }

    @Override
    public void executeStrategy() {

        Collections.sort(potentialMarkets, new Comparators.MarketRsiComparatorAscending());
        for(Market m: potentialMarkets){
            if(positionManager.contains(m.getMarketCurrency())) evaluateSell(m);
            evaluateBuy(m);
        }
        for(Position p: positionManager.getPositionBooks().values()){
            Market current = getMarketBook().getMarket(Constants.buildBtcMarketName(p.getCurrency()));
        }
    }



    private double calculateBuyAmount(Market market){
        double ask = market.getAsk();
        return tradingMinimum/ask;
    }




    @Override
    public void evaluateBuy(Market market) {
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
    public void evaluateSell(Market market) {
        if(okToSell(market))     orderManager.initiateSell(market.getMarketName());

    }



    @Override
    protected boolean okToSell(Market market) {
        double rsi = market.getCurrentRSI();
        double bollingerLow = market.getBollingerLow();
        double currentPrice = market.getLast();
        double pAndL = orderManager.getPandL(market);

        if( (rsi>= rsiOverBought  && currentPrice>bollingerLow) || pAndL<= stopLoss || pAndL>= profitTaking) return true;
        else     return false;
    }





}
