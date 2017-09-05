package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;



public class BalanceAdapter {

    ExchangeInterface client;


    @Autowired
    public BalanceAdapter(ExchangeInterface client){
        this.client = client;
    }


    public Double getBalance(String currency){
        Response response = client.getBalance(currency);
        Double result = null;
        if(JsonParserUtil.isAsuccess(response)){
            JsonObject jsonObject = JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
            result = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.AVAILABLE);
        }


        return result;
    }



}
