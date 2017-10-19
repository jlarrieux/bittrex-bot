package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Trading.SideWaysTrader;
import com.jlarrieux.bittrexbot.UseCaseLayer.Trading.TrendingTrader;
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


    @Autowired
    private SideWaysTrader sideWaysTrader;


    @Autowired
    private TrendingTrader trendingTrader;



    @Autowired
    public Decider( TradingProperties tradingProperties, SideWaysTrader sideWaysTrader, TrendingTrader trendingTrader){
        this.sideWaysTrader = sideWaysTrader;
        adxTrendThreshold = tradingProperties.getAdxTrendThreshold();
        this.trendingTrader = trendingTrader;
    }







    public void evaluate(Markets marketBooks){
        this.markets = marketBooks;
        sideWaysTrader.clearAllTempMarkets();
        trendingTrader.clearAllTempMarkets();
        sideWaysTrader.setMarketBook(marketBooks);
        trendingTrader.setMarketBook(marketBooks);
        buildPotentialBuyMarkets(marketBooks);

        executeStrategy();
    }



    private void buildPotentialBuyMarkets(Markets marketBooks) {
        for(String key: marketBooks.keySet()){
            Market market = marketBooks.get(key);
            double adx = market.getAdxValue();
            if(adx>= adxTrendThreshold) trendingTrader.addToPotentialMarkets(market);
            else if(adx<adxTrendThreshold) sideWaysTrader.addToPotentialMarkets(market);
        }
    }








    private void executeStrategy(){
        sideWaysTrader.executeStrategy();
        trendingTrader.executeStrategy();


    }







}
