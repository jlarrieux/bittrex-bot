package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;



@Data
public class OrderBookEntry {


    private double quantity, price;


    public OrderBookEntry(double quantity, double price){
        this.quantity = quantity;
        this.price = price;
    }

    public OrderBookEntry(JsonObject jsonObject){
        quantity = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.QUANTITY);
        price = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.RATE_CapitalFIRST);
    }


}
