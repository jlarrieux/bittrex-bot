package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;



public abstract class AbstractTrader implements Trader {

    protected PositionManager positionManager;
    protected OrderManager orderManager;
    protected double stopLoss, tradingMinimum, profitTaking;
    protected List<Market> potentialBuyMarkets = new ArrayList<>();
    protected List<Market> potentialSellMarkets = new ArrayList<>();
    private Markets marketBook;
    protected double adxTrendThreshold;




    @Autowired
    public AbstractTrader(TradingProperties tradingProperties, OrderManager orderManager){
        this.marketBook = new Markets();
        this.positionManager = orderManager.getPositionManager();
        this.orderManager = orderManager;
        stopLoss = tradingProperties.getStopLoss();
        profitTaking = tradingProperties.getProfitTaking();
        tradingMinimum = tradingProperties.getMinimumBtc();
        adxTrendThreshold = tradingProperties.getAdxTrendThreshold();
    }

    public void clearAllTempMarkets(){
        potentialBuyMarkets.clear();

    }


    public void addToPotentialMarkets(Market m){
        potentialBuyMarkets.add(m);
    }

    public void addToPotentialSellMarkets(Market m){
        potentialSellMarkets.add(m);
    }

    public void setMarketBook(Markets marketBook){
        this.marketBook = marketBook;
    }

    public Markets getMarketBook(){
        return marketBook;
    }


    protected abstract boolean okToBuy(Market m);

    protected abstract boolean okToSell(Market m);



}
