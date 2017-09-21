package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Log
@Data
@Component
public class MarketSummaryAdapter {



    ExchangeInterface client;
    private double last,bid, ask, high , low, volume;
    private int openBuyOrders, openSellOrders;



    @Autowired
    private MarketSummaryAdapter(ExchangeInterface client){
        this.client = client;
    }

    public JsonArray getMarketSummaries(){
        Response response = client.getMarketSummaries();
        if(JsonParserUtil.isAsuccess(response)) return JsonParserUtil.getJsonArrayFromJsonString(response.getResult());
        return null;
    }

    public Response getMarketSummaries_2(){
        Response response = client.getMarketSummaries();

        return response;
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






}
