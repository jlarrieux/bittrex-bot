package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.BalanceAdapter;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Log
@Component
public class PortFolio {

    private Positions positionBooks = new Positions();
    private MarketSummaryAdapter marketSummaryAdapter;
    private double total, initialFirstValue;
    private PositionManager positionManager;




    @Autowired
    public PortFolio(PositionManager manager, MarketSummaryAdapter marketSummaryAdapter, BalanceAdapter balanceAdapter){
        this.positionManager = manager;
        this.marketSummaryAdapter = marketSummaryAdapter;
        initialFirstValue = balanceAdapter.getBalance(Constants.BTC.toUpperCase());
    }

    public double getCurrentPortFolioValue(){
        total=0;
        buildPositionBooks();
        return  total;
    }


    private void buildPositionBooks(){
        positionBooks.clear();
        Collection<Position> allPostions =  positionManager.getPositionBooks().values();
        if(allPostions!=null){
            for(Position p: allPostions) positionBooks.put(p.getCurrency(), p);
            calculateTotal();
        }

    }



    private void calculateTotal(){
        for(String key: positionBooks.keySet()){
            Position p = positionBooks.get(key);
            lowLevelCalculation(p);
        }

    }


    private void lowLevelCalculation(Position p){
        marketSummaryAdapter.executeMarketSearch(Constants.buildBtcMarketName(p.getCurrency()));
        double current = p.getQuantity() * marketSummaryAdapter.getLast();
        total += current;
    }

    public double profitAndLossPercentage(){
        return (total-initialFirstValue)/initialFirstValue;
    }



    public PositionManager getPositionManager() {
        return positionManager;
    }
}
