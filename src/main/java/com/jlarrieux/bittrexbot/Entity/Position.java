package com.jlarrieux.bittrexbot.Entity;

import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
public class Position {


    private String currency;
    private double balance, available, pending, purchasedPrice, lastPrice;

    public Position(){

    }

    public Position(JsonObject object){
        buildPosition(object);
    }



    private void buildPosition(JsonObject object) {
//        log.info("Printing object: "+ object.toString());
        JsonObject balanceContainer = object.getAsJsonObject(Constants.BALANCE);
        balance = JsonParserUtil.getDoubleFromJsonObject(balanceContainer, Constants.BALANCE);
        available = JsonParserUtil.getDoubleFromJsonObject(balanceContainer, Constants.AVAILABLE);
        currency = JsonParserUtil.getStringFromJsonObject(balanceContainer, "Currency");
    }
}

