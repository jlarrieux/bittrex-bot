package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Comparators;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.Util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class TrendingTrader extends AbstractTrader {


    @Autowired
    public TrendingTrader(TradingProperties tradingProperties, OrderManager orderManager) {
        super(tradingProperties,  orderManager);
    }


    @Override
    public void executeStrategy() {
        Collections.sort(potentialBuyMarkets, new Comparators.MarketKeltnerToLastPriceAscending());
        for(Market m: potentialBuyMarkets){
            evaluateBuy(m);
        }
        potentialBuyMarkets.clear();
        for(Position p: positionManager.getPositionBooks().values()){
            Market current = getMarketBook().getMarket(Constants.buildBtcMarketName(p.getShortCurrency()));
            double adx = current.getAdxValue();
            if(adx>adxTrendThreshold)evaluateSell(current);
        }
    }



    @Override
    public void evaluateBuy(Market market) {
        if(okToBuy(market)) orderManager.initiateBuy(market);
    }


    @Override
    protected boolean okToBuy(Market m) {
        boolean isUpTrend = m.getAdx().getADXDirection()>0;
        boolean isAboveKeltnerHigh = m.isPriceAboveKeltnerHigh();
        if(isUpTrend && isAboveKeltnerHigh) return true;
        else return false;
    }





    @Override
    public void evaluateSell(Market market) {
        if(okToSell(market)) orderManager.initiateSell(market, market.getMarketCurrency());

    }



    @Override
    protected boolean okToSell(Market m) {
        boolean isDownTrend = m.getAdx().getADXDirection()<0;
        double pAndL = orderManager.getPandL(m);
        if((isDownTrend && m.isPriceBelowKeltnerLow()) || pAndL<=stopLoss || pAndL>= profitTaking) return true;
        else return false;
    }

}
