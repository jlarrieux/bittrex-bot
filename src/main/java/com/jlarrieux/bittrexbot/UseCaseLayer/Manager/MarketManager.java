package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
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



    @Autowired
    public MarketManager(JLarrieuxRestClient client, Decider decider , Markets markets) {
        jLarrieuxRestClient = client;
        this.decider = decider;
        marketBooks = markets;
        getMarketCount();
    }

    public MarketManager(){
        marketBooks = new Markets();
    }


    public void addMarkets(JsonArray array){
//        log.info(String.valueOf(marketBooks.size()));
   //     if(marketBooks.size()==0)    getDataFromServer();

        iterate(array);

    }

    private void iterate(JsonArray array){

        for(int i=0; i<array.size(); i++){
            marketBooks.add(array, i);
        }
        if(decider!=null) decider.evaluate(marketBooks);
    }



    private void getDataFromServer(){
        long start = System.currentTimeMillis();
        log.info("~~~~~~~~Getting 1 time data from server...this may take up to 5 minutes...~~~~~~~~~~");
        for(int i=1; i<marketCount+1; i++){

            Market market = new Market();
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(jLarrieuxRestClient.getMarketById(i));
            market.alternateConstruction(object);
            log.info(String.format("Downloaded market #%s out of %s (%s)", Constants.addSpaceForInt(i, ""),Constants.addSpaceForLong( marketCount+1,""), market.getMarketCurrency()));
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
            return String.format("Coin: %s\nHigh: %f\t low: %f\t volume: %f\t last priceQueue: %f\n# of open buys: %d\t# of open sells: %d\tspread: %f\ncurrentRSI: %f\n bollinger: %s",
                    m.getMarketCurrency(), m.getHigh(), m.getLow(), m.getVolume(), m.getLast(), m.getOpenBuyOrders(), m.getOpenSellOrders(),m.getSpread(), m.getCurrentRSI(), m.bollingerToString());
        }
        else return String.format("No such '%s' exists", marketName);
    }























}