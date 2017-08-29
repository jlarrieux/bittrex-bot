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
    private double  available,  averagePurchasedPrice,  quantity, total=0;


    public Position(){

    }

    public Position(JsonObject object){
        buildPosition(object);
    }


    public Position(Order order, String currency){

        this.currency = currency;
        available = order.getQuantity();
        averagePurchasedPrice = order.getLimit();
        quantity = order.getQuantity();
        total = quantity *averagePurchasedPrice;


    }

    public Position(double quantity, double purchasedPrice, String marketCurrency){
        this.quantity = quantity;
        if(total==0) this.averagePurchasedPrice = purchasedPrice;
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
        currency =Constants.buildBtcMarketName(currency);

    }

}

