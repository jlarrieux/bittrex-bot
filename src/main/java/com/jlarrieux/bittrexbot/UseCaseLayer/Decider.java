package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.jlarrieux.bittrexbot.Entity.*;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Log
@Component
@Data
public class Decider {


    private PositionManager positionManager;
    private double beginingBTCbalance, currentBTCbalance;
    public double  rsiOverSold, spreadThreshold;
    private List<Market> potentialBuys = new ArrayList<>();
    private List<Market> potentialSells = new ArrayList<>();
    private Markets myOrderBooks;
    private PortFolio portFolio;
    private Trader trader;
    private StringBuilder portfolioStatement= new StringBuilder("NOTHING YET") ;


    @Autowired
    public Decider(PositionManager positionManager,  TradingProperties tradingProperties,  Trader trader, PortFolio portFolio){
        this.trader = trader;
        spreadThreshold = tradingProperties.getSpread();
        rsiOverSold = tradingProperties.getRsiOverSold();
        this.positionManager = positionManager;
        this.portFolio = portFolio;
        cleanSlate();
        beginingBTCbalance = portFolio.getCurrentPortFolioValue();
        currentBTCbalance = beginingBTCbalance;

    }


    @Scheduled(fixedRate = 60000)
    private void updateBalance(){
        currentBTCbalance = portFolio.getCurrentPortFolioValue();
        portfolioStatement = new StringBuilder(String.format("Begining Portfolio balance was: %f \tcurrent PortfolioBalance is: %f that's a change of  %.2f%%", beginingBTCbalance, currentBTCbalance,calculateBalancePercentChange()));
        log.info(positionManager.toString());
    }


    @Scheduled(fixedDelay = 36000000)
    private void sellingAll(){
        trader.cleanUp();
    }




    public void evaluate(Markets orderBooks){
        this.myOrderBooks = orderBooks;
        potentialBuys.clear();
        potentialSells.clear();
        boolean ownPosition= true;
        for(String key: orderBooks.keySet()){
            Market market = orderBooks.get(key);
            double rsi = market.getCurrentRSI();
            double spread = market.getSpread();
            if(rsi<rsiOverSold && spread>=spreadThreshold) potentialBuys.add(market);
            if(ownPosition(market))  potentialSells.add(market);
        }
        Collections.sort(potentialBuys, new Comparators.MarketRsiComparatorAscending());
        Collections.sort(potentialSells, new Comparators.MarketRsiComparatorDescending());
        executeStrategy();
    }





    private boolean ownPosition(Market market){
        return positionManager.getPositionBooks().containsKey(market.getMarketName());
    }



    private void executeStrategy(){

        trader.makeTrade(potentialBuys, potentialSells);
        currentBTCbalance = portFolio.getCurrentPortFolioValue();
        printPositionBooks();

    }



    private double calculateBalancePercentChange(){
        return (currentBTCbalance-beginingBTCbalance)*100/beginingBTCbalance;
    }


    private void printPositionBooks(){
        List<Position> positionList = new ArrayList<>(positionManager.getPositionBooks().values());
        for(Position p: positionList){
            double currentPrice = myOrderBooks.get(p.getMarketName()).getLast();
            double purchasedPrice = p.getAveragePurchasedPrice();
            p.setPAndL((currentPrice-purchasedPrice)/purchasedPrice);
        }

        Collections.sort(positionList, new Comparators.PositionPandLComparatorDescending());
        for(Position p: positionList){
            double quantity = p.getQuantity();
            double purchasedPrice = p.getAveragePurchasedPrice();
            log.info(String.format("Position---- Coin name: %s \t profit and loss: %s\t Average price: %s \t quantity: %s \ttotal value: %f btc",
                    Constants.addSpace(p.getCurrency()), Constants.addSpaceForDouble(p.getPAndL(),2,"%"), Constants.addSpaceForDouble(purchasedPrice, Constants.CURRENCY_PRECISION, Constants.BTC), Constants.addSpaceForDouble(quantity, Constants.CURRENCY_PRECISION, ""), purchasedPrice* quantity));
        }

        log.info("\n\n");
    }



    public void cleanSlate(){
        trader.cleanSlate();

    }

}
