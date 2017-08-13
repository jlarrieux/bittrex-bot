package com.jlarrieux.bittrexbot.Manager;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Decider;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.REST.JLarrieuxRestClient;
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

    Market market;

    private long marketCount;

    @Autowired
    public MarketManager(JLarrieuxRestClient client, Decider decider , Markets markets) {
        jLarrieuxRestClient = client;
        this.decider = decider;
        marketBooks = markets;
        getMarketCount();
    }



    public void addMarkets(JsonArray array){
//        log.info(String.valueOf(marketBooks.size()));
        if(marketBooks.size()==0)    getDataFromServer();

        iterate(array);

    }

    private void iterate(JsonArray array){
        Market market;
        for(int i=0; i<array.size(); i++){
            market = marketBooks.add(array, i);
//            if(market!=null) log.info(String.format("%d- %s size of queue: %d %n %s%n%S%n%n",i,market.getMarketName()+"\t",market.getPriceQueue().getN(), market.bollingerToString(), market.RSItoString()));
//            else log.warning("null market");

        }
        decider.evaluate(marketBooks);
    }

    private void getDataFromServer(){
        for(int i=1; i<marketCount+1; i++){
            market = new Market();
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(jLarrieuxRestClient.getMarketById(i));
            market.setMarketCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_CURRENCY_SHORT));
            market.setBaseCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.BASE_CURRENCY_SHORT));
            market.setMarketName(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_NAME_SHORT));
            market.setActive(JsonParserUtil.getBooleanFromJsonObject(object, Constants.ACTIVE));
            market.setMinTradeSize(JsonParserUtil.getDoubleFromJsonObject(object, Constants.MIN_TRADE_SIZE_SHORT));

            JsonArray databook = object.getAsJsonArray(Constants.DATA_BOOK);
            iterateFromServerData(databook);
//            log.info(market.toString());
            marketBooks.add(market);
        }
    }

    private void iterateFromServerData(JsonArray jsonArray){
        JsonObject object = null;
        for(int i=0; i<jsonArray.size();i++){
            object = (JsonObject) jsonArray.get(i);
            double price = JsonParserUtil.getDoubleFromJsonObject(object, Constants.LAST.toLowerCase());
            if(i==0) market.setLast(price);
            else market.updatePrice(price);
        }
        market.setHigh(JsonParserUtil.getDoubleFromJsonObject(object,Constants.HIGH.toLowerCase()));
        market.setLow(JsonParserUtil.getDoubleFromJsonObject(object,Constants.LOW.toLowerCase()));
        market.setBid(JsonParserUtil.getDoubleFromJsonObject(object,Constants.BID.toLowerCase()));
        market.setAsk(JsonParserUtil.getDoubleFromJsonObject(object,Constants.ASK.toLowerCase()));
        market.setVolume(JsonParserUtil.getDoubleFromJsonObject(object,Constants.VOLUME.toLowerCase()));
        market.setSpread(JsonParserUtil.getDoubleFromJsonObject(object, Constants.SPREAD));
        market.setOpenBuyOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_BUY_ORDERS_SHORT));
        market.setOpenSellOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_SELL_ORDERS_SHORT));

    }

    private void getMarketCount(){
        String response = jLarrieuxRestClient.getCount();
        JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(response);
        marketCount = JsonParserUtil.getLongFromJsonObject(object,"numberOfMarkets");
        log.info(String.valueOf(marketCount));
    }


}