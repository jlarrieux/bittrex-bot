package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;



public abstract class AbstractTrader implements Trader {

    private PositionManager positionManager;
    private OrderManager orderManager;
    private ExchangeInterface client;
    private double stopLoss, tradingMinimum, profitTaking;
    protected List<Market> potentialMarkets = new ArrayList<>();





    @Autowired
    public AbstractTrader(ExchangeInterface client, TradingProperties tradingProperties, PositionManager positionManager, OrderManager orderManager){
        this.client = client;
        this.positionManager = positionManager;
        this.orderManager = orderManager;
        stopLoss = tradingProperties.getStopLoss();
        profitTaking = tradingProperties.getProfitTaking();
        tradingMinimum = tradingProperties.getMinimumBtc();
    }

    public void clearAllTempMarkets(){
        potentialMarkets.clear();

    }


    public void addToPotentialMarkets(Market m){
        potentialMarkets.add(m);
    }




    protected abstract boolean okToBuy(Market m);
    protected abstract boolean okToSell(Market m);


}
