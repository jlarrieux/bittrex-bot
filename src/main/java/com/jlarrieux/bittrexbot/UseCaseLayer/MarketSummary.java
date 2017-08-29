package com.jlarrieux.bittrexbot.UseCaseLayer;



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
public class MarketSummary {



    ExchangeInterface client;

    private double last,bid, ask;

    @Autowired
    private MarketSummary(ExchangeInterface client){
        this.client = client;
    }


    public void executeMarketSearch(String marketName){
        build(marketName);
    }



    private void build(String marketName){
        Response response = client.getMarketSummary(marketName);
        if(JsonParserUtil.isAsuccess(response) && !response.getResult().contains(Constants.NULL)) populate(response.getResult());


    }


    private void populate(String jsonString){
        JsonObject jsonObject = JsonParserUtil.getJsonObjectFromJsonString(jsonString);

        if(jsonObject!=null  ) {
            boolean bidIsNull = jsonObject.get(Constants.BID).toString().contains(Constants.NULL);
            if(!bidIsNull){
                last = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.LAST);
                bid = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.BID);
                ask =JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.ASK);
            }
        }
    }






}
