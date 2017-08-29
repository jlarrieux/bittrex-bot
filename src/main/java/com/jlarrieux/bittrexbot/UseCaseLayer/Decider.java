package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.Trading.SideWaysTrader;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Log
@Component
@Data
public class Decider {



    public double  adxTrendThreshold;

    private Markets markets;
//    private PortFolio portFolio;

    @Autowired
    private SideWaysTrader sideWaysTrader;

//    @Autowired
//    private Trend
//    private StringBuilder portfolioStatement= new StringBuilder("NOTHING YET") ;


    @Autowired
    public Decider(PositionManager positionManager, TradingProperties tradingProperties, SideWaysTrader sideWaysTrader){
        this.sideWaysTrader = sideWaysTrader;
        adxTrendThreshold = tradingProperties.getAdxTrendThreshold();


    }


//    @Scheduled(fixedRate = 60000)
//    private void updateBalance(){
//        currentBTCbalance = portFolio.getCurrentPortFolioValue();
////        portfolioStatement = new StringBuilder(String.format("Begining Portfolio balance was: %f \tcurrent PortfolioBalance is: %f that's a change of  %.2f%%", beginingBTCbalance, currentBTCbalance,calculateBalancePercentChange()));
//        log.info(positionManager.toString());
//    }






    public void evaluate(Markets orderBooks){
        this.markets = orderBooks;
        boolean ownPosition= true;
        for(String key: orderBooks.keySet()){
            Market market = orderBooks.get(key);
            double adx = market.getAdxValue();
            if(adx>= adxTrendThreshold) ;
            else sideWaysTrader.addToPotentialMarkets(market);
        }
//        Collections.sort(potentialBuys, new Comparators.MarketRsiComparatorAscending());
//        Collections.sort(potentialSells, new Comparators.MarketRsiComparatorDescending());
        executeStrategy();
    }







    private void executeStrategy(){
        sideWaysTrader.executeStrategy();
//        sideWaysTrader.makeTrade(potentialBuys, potentialSells);
//        currentBTCbalance = portFolio.getCurrentPortFolioValue();
//        printPositionBooks();

    }


//
//    private double calculateBalancePercentChange(){
//        return (currentBTCbalance-beginingBTCbalance)*100/beginingBTCbalance;
//    }


//    private void printPositionBooks(){
//        List<Position> positionList = new ArrayList<>(positionManager.getPositionBooks().values());
//        for(Position p: positionList){
//            double currentPrice = markets.get(p.getMarketName()).getLast();
//            double purchasedPrice = p.getAveragePurchasedPrice();
//            p.setPAndL((currentPrice-purchasedPrice)/purchasedPrice);
//        }
//
//        Collections.sort(positionList, new Comparators.PositionPandLComparatorDescending());
//        for(Position p: positionList){
//            double quantity = p.getQuantity();
//            double purchasedPrice = p.getAveragePurchasedPrice();
//            log.info(String.format("Position---- Coin name: %s \t profit and loss: %s\t Average priceQueue: %s \t quantity: %s \ttotal value: %f btc",
//                    Constants.addSpace(p.getCurrency()), Constants.addSpaceForDouble(p.getPAndL(),2,"%"), Constants.addSpaceForDouble(purchasedPrice, Constants.CURRENCY_PRECISION, Constants.BTC), Constants.addSpaceForDouble(quantity, Constants.CURRENCY_PRECISION, ""), purchasedPrice* quantity));
//        }
//
//        log.info("\n\n");
//    }





}
