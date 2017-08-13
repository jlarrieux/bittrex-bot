package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Manager.PositionManager;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.REST.BittrexRestClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Log
@Component
@Data
public class Decider {



    PositionManager positionManager;
    private double beginingBTCbalance, currentBTCbalance;
    private BittrexRestClient client;
    private double stopLoss, profitTaking, spreadThreshold, rsiOverBought, rsiOverSold, rsiNoMomentum;
    private List<Market> potentialBuys = new ArrayList<>();
    private List<Market> potentialSells = new ArrayList<>();


    @Autowired
    public Decider(PositionManager positionManager, BittrexRestClient client, TradingProperties tradingProperties){
        stopLoss = tradingProperties.getStopLoss();
        profitTaking = tradingProperties.getProfitTaking();
        spreadThreshold = tradingProperties.getSpread();
        rsiOverBought = tradingProperties.getRsiOverBought();
        rsiOverSold = tradingProperties.getRsiOverSold();
        rsiNoMomentum = tradingProperties.getRsiNoMomentum();
        this.positionManager = positionManager;
        this.client = client;
        getBalance();
    }

    private void getBalance(){
        Response response = client.getBalance("BTC");
        if(JsonParserUtil.isAsuccess(response)){
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
            beginingBTCbalance = JsonParserUtil.getDoubleFromJsonObject(object,"Available");
            currentBTCbalance = beginingBTCbalance;
            log.info(String.format("Printing balanceContainer available: %f btc", beginingBTCbalance));
        }
    }


    public void evaluate(Markets orderBooks){
        potentialBuys.clear();
        boolean ownPosition= true;
        for(String key: orderBooks.keySet()){
            Market market = orderBooks.get(key);
            double rsi = market.getRSI();
            double spread = market.getSpread();
            if(rsi<rsiOverSold && spread>=spreadThreshold) potentialBuys.add(market);
            if(rsi> rsiOverBought && ownPosition)  potentialSells.add(market);
        }
        Collections.sort(potentialBuys, new MarketComparators.MarketRsiComparatorAscending());
        Collections.sort(potentialSells, new MarketComparators.MarketRsiComparatorDescending());
        showSortedList();
    }

    private void showSortedList(){
        int i=1, j=1;
        log.info("%n%n%n%nPotential Buys:");
        for(Market market: potentialBuys){
            log.info(String.format("%d- %s size of queue: %d %n %s%n%S%n%n",i,market.getMarketName()+"\t",market.getPriceQueue().getN(), market.bollingerToString(), market.RSItoString()));
            i++;
        }
        log.info("%n%n%n%nPotentail Sells");
        for(Market market: potentialSells){
            log.info(String.format("%d- %s size of queue: %d %n %s%n%S%n%n",j,market.getMarketName()+"\t",market.getPriceQueue().getN(), market.bollingerToString(), market.RSItoString()));
            j++;
        }

    }














}
