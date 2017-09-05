package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Data
@Component
public class MarketOrderBookAdapater {


    ExchangeInterface client;
    List<OrderBookEntry> buyOrderBookEntries = new ArrayList<>();
    List<OrderBookEntry> sellOrderBookEntries = new ArrayList<>();

    @Autowired
    private MarketOrderBookAdapater(ExchangeInterface client){
        this.client = client;

    }

    public void executeMarketOrderBook(String marketName){
        build(marketName);
    }


    private void build(String marketName){
        Response response = client.getMarketOrderBook(marketName);
        if(JsonParserUtil.isAsuccess(response) && !response.getResult().contains(Constants.NULL)) populate(response.getResult());
    }



    private void populate(String jsonString) {
        JsonObject jsonObject = JsonParserUtil.getJsonObjectFromJsonString(jsonString);
        if(jsonObject!=null){
            JsonArray buy = jsonObject.getAsJsonArray(Constants.BUY);
            JsonArray sell = jsonObject.getAsJsonArray(Constants.SELL);
            populateBuy(buy);
            populateSell(sell);


        }
    }

    private void populateBuy(JsonArray buy){
        for(JsonElement element: buy){
            JsonObject object = element.getAsJsonObject();
            buyOrderBookEntries.add(new OrderBookEntry(object));

        }
    }


    private void populateSell(JsonArray sell){
        for(JsonElement element: sell){
            JsonObject object = element.getAsJsonObject();
            sellOrderBookEntries.add(new OrderBookEntry(object));

        }
    }


}
