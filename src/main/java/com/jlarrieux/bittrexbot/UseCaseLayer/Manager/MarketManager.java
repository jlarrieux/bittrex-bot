package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.Properties.TradingProperties;
import com.jlarrieux.bittrexbot.REST.JLarrieuxRestClient;
import com.jlarrieux.bittrexbot.UseCaseLayer.Decider;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Log
@Component
@Getter
public class MarketManager {



    private Markets marketBooks;
    private Decider decider;
    private JLarrieuxRestClient jLarrieuxRestClient;
    private long marketCount;
    private double adxTrendThreshold;


    @Autowired
    public MarketManager(JLarrieuxRestClient client, Decider decider , Markets markets, TradingProperties tradingProperties) {
        jLarrieuxRestClient = client;
        this.decider = decider;
        marketBooks = markets;
        adxTrendThreshold = tradingProperties.getAdxTrendThreshold();
        getMarketCount();
    }

    public MarketManager(){
        marketBooks = new Markets();
    }


    public void addMarkets(JsonArray array){
//        log.info(String.valueOf(marketBooks.size()));
   //     if(marketBooks.size()==0)    getDataFromServer();

        log.info("Inside: " + getClass().getSimpleName() +"\t Method: " + "addMarkets(JsonArray)" );
        iterate(array);


    }

    private void iterate(JsonArray array){
        log.info("Inside: " + getClass().getSimpleName() +"\t Method: " + "iterate(JsonArray)" );
        for(int i=0; i<array.size(); i++){
            marketBooks.add(array, i);
            log.info(i+"- Inside: " + getClass().getSimpleName() +"\t Method: " + "iterate(JsonArray) in for loop! Curency is: " +
                    (new Market((JsonObject) array.get(i))).getMarketName() );
        }
        if(decider!=null) decider.evaluate(marketBooks);
    }



    private void getDataFromServer(){
        long start = System.currentTimeMillis();
        log.info("~~~~~~~~Getting 1 time data from server...this may take up to 5 minutes...~~~~~~~~~~");
        for(int i=1; i<marketCount+1; i++){
            long s = System.currentTimeMillis();
            Market market = new Market();
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(jLarrieuxRestClient.getMarketById(i));
            market.alternateConstruction(object, true);
            log.info(String.format("Downloaded market #%s out of %s (%s, %S)\t%.2f seconds",
                    Constants.addSpaceForInt(i, ""),Constants.addSpaceForLong( marketCount+1,""), market.getMarketCurrency(),market.getMarketName(), Double.valueOf(System.currentTimeMillis()-s)/1000));
//            log.info(market.toString());
            marketBooks.add(market);
        }
        log.info("--------------------Done with server update!------------------------------");
    }



    private void getMarketCount(){
        String response = jLarrieuxRestClient.getCount();
        JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(response);
        marketCount = JsonParserUtil.getLongFromJsonObject(object,"numberOfMarkets");
        log.info(String.valueOf(marketCount));
    }

    public String getCoinInsight(String coin){
        String marketName = Constants.buildBtcMarketName(coin);
        if(marketBooks.containsKey(marketName)){
            Market m = marketBooks.get(marketName);
            StringBuilder suffix = new StringBuilder();
            if(m.getAdxValue()>=adxTrendThreshold) suffix.append(String.format("KeltnerHigh: %f\t&emsp;&emsp;KeltnerMid: %f\t&emsp;&emsp;KeltnerLow: %f<br>\n", m.getKeltnerChannels().getHigh(), m.getKeltnerChannels().getMid(), m.getKeltnerChannels().getLow()));
            else suffix.append(String.format("RSI value: %f\n<br>Bollinger: %s", m.getCurrentRSI(), m.getBollingerSMA().toString()));


            return String.format("Coin: %s\n<br>High: %f\t&emsp;&emsp; low: %f\t&emsp;&emsp; volume: %f\t&emsp;&emsp; last priceQueue: %f\n<br># of open buys: %d\t&emsp;&emsp;# of open sells: %d\t&emsp;&emsp;spread: %f\n<br>Adx value: %f\t&emsp;&emsp;with direction: %f\n<br>%s",
                    m.getMarketCurrency(), m.getHigh(), m.getLow(), m.getVolume(), m.getLast(), m.getOpenBuyOrders(), m.getOpenSellOrders(),m.getSpread(),m.getAdxValue(),m.getAdx().getADXDirection(), suffix.toString());
        }
        else return String.format("Market with name '%s' does not exists!", marketName);
    }


    public Market getMarket(String coin){
        return marketBooks.get(Constants.buildBtcMarketName(coin));
    }



    public String getANegativeDirection(){
        StringBuilder builder = new StringBuilder("None");
        for(String key: marketBooks.keySet()){
            Market m = marketBooks.get(key);
            if(m.getAdx().getADXDirection()<0){
                log.info(String.format("Negative direction with value :%f", m.getAdx().getADXDirection()));
                builder = new StringBuilder();
                builder.append(String.format("Coin: %s\n<br>High: %f\t&emsp;&emsp; low: %f\t&emsp;&emsp; volume: %f\t&emsp;&emsp; last priceQueue: %f\n<br># of open buys: %d\t&emsp;&emsp;# of open sells: %d\t&emsp;&emsp;spread: %f\n<br>Adx value: %f\t&emsp;&emsp;with direction: %f\n<br>",
                        m.getMarketCurrency(), m.getHigh(), m.getLow(), m.getVolume(), m.getLast(), m.getOpenBuyOrders(), m.getOpenSellOrders(),m.getSpread(),m.getAdxValue(),m.getAdx().getADXDirection()));
                break;
            }
        }

        return builder.toString();
    }

















}