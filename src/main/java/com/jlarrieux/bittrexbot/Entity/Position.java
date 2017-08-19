package com.jlarrieux.bittrexbot.Entity;

import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
public class Position {


    private String currency, marketName;
    private double  available, pending, averagePurchasedPrice, lastPrice, quantity, total, pAndL;


    public Position(){

    }

    public Position(JsonObject object){
        buildPosition(object);
    }

    public Position(double quantity, double purchasedPrice, String marketCurrency){
        this.quantity = quantity;
        this.averagePurchasedPrice = purchasedPrice;
        total = quantity * purchasedPrice;
        this.currency = marketCurrency;
    }


    private void buildPosition(JsonObject object) {
        log.info(object.toString());
        JsonObject balanceContainer = object.getAsJsonObject(Constants.BALANCE);
        quantity = JsonParserUtil.getDoubleFromJsonObject(balanceContainer, Constants.BALANCE);
        available = JsonParserUtil.getDoubleFromJsonObject(balanceContainer, Constants.AVAILABLE);
        currency = JsonParserUtil.getStringFromJsonObject(balanceContainer, "Currency");
    }


    public void update(double quantityNew, double priceNew){
        total = total+(quantityNew*priceNew);
        quantity = quantityNew + quantity;
        averagePurchasedPrice = total/quantity;
    }

    public void alternatBuild(JsonObject object){
        currency = JsonParserUtil.getStringFromJsonObject(object, Constants.upperCaseFirst(Constants.CURRENCY));
        quantity = JsonParserUtil.getDoubleFromJsonObject(object, Constants.BALANCE);
        available = JsonParserUtil.getDoubleFromJsonObject(object,Constants.AVAILABLE);
        marketName =Constants.buildBtcMarketName(currency);

    }

}

