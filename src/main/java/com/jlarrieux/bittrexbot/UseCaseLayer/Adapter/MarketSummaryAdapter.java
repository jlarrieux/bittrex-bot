package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;



@Log
@Data
@Component
public class MarketSummaryAdapter {



    ExchangeInterface client;
    private double last,bid, ask, high , low, volume;
    private int openBuyOrders, openSellOrders;
    private HashMap<String, MarketInternal> marketHashMap = new HashMap<>();



    @Autowired
    private MarketSummaryAdapter(ExchangeInterface client){
        this.client = client;
    }

    public JsonArray getMarketSummaries(){
        Response response = client.getMarketSummaries();
        if(JsonParserUtil.isAsuccess(response)) return JsonParserUtil.getJsonArrayFromJsonString(response.getResult());
        return null;
    }
public void executeMarketSearch(String marketName){
        build(marketName);
    }

    private void build(String marketName){
        Response response = client.getMarketSummary(marketName);
        if(JsonParserUtil.isAsuccess(response) && !response.getResult().contains(Constants.NULL)) populate(response.getResult());


    }

    private void populate(String jsonString){
        JsonArray array = JsonParserUtil.getJsonArrayFromJsonString(jsonString);
        JsonObject jsonObject = (JsonObject) array.get(0);

        if(jsonObject!=null  ) {
            boolean bidIsNull = jsonObject.get(Constants.BID).toString().contains(Constants.NULL);
            if(!bidIsNull){
                log.info(jsonObject.toString());
                last = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.LAST);
                bid = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.BID);
                ask =JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.ASK);
                high =JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.HIGH);
                low = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.LOW);
                volume = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.VOLUME);
                openBuyOrders = JsonParserUtil.getIntFromJsonObject(jsonObject, Constants.OPEN_BUY_ORDERS);
                openSellOrders = JsonParserUtil.getIntFromJsonObject(jsonObject,Constants.OPEN_SELL_ORDERS);
            }
        }
    }


    public String getMarketCurrency(String marketName){
        if(marketHashMap.containsKey(marketName)) return marketHashMap.get(marketName).getMarketCurrency();
        else{
            Response response = client.getMarkets();
            if(JsonParserUtil.isAsuccess(response)){
                populateMarketHashMap(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
                return marketHashMap.get(marketName) == null ?
                                                        null:
                        marketHashMap.get(marketName).getMarketCurrency();
            }
        }

        return null;
    }

    private void populateMarketHashMap(JsonArray jsonArray){
        MarketInternal market ;
        for(JsonElement element : jsonArray){
            JsonObject jsonObject = (JsonObject) element;
//            System.out.println(jsonObject);
            market = new MarketInternal(jsonObject);

            marketHashMap.put(market.getMarketName(),market);
        }
    }




    @Data
    private class MarketInternal{
        private String marketCurrency, marketCurrencyLong, marketName;
        private double minTradeSize;
        private boolean isActive;


        public MarketInternal(JsonObject jsonObject){
            populate(jsonObject);
        }

        private void populate(JsonObject object){
           setMarketCurrency(JsonParserUtil.getStringFromJsonObject(object,Constants.upperCaseFirst(Constants.MARKET_CURRENCY_SHORT)));
           setMarketCurrencyLong(JsonParserUtil.getStringFromJsonObject(object, Constants.upperCaseFirst(Constants.MARKET_CURRENCY_LONG)));
           setMarketName(JsonParserUtil.getStringFromJsonObject(object,Constants.MARKET_NAME));
           setMinTradeSize(JsonParserUtil.getDoubleFromJsonObject(object,Constants.MIN_TRADE_SIZE));
           setActive(JsonParserUtil.getBooleanFromJsonObject(object,Constants.IS_ACTIVE));
        }

    }


}
