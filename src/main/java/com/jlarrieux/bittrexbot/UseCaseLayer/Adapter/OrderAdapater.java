package com.jlarrieux.bittrexbot.UseCaseLayer.Adapter;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.REST.ExchangeInterface;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class OrderAdapater {

    ExchangeInterface client;

    enum orderType{
        Buy, Sell;
    }



    @Autowired
    public OrderAdapater(ExchangeInterface client){
        this.client = client;
    }



    public boolean cancelOrder(String uuid){
        Response response = client.cancelOrder(uuid);
        return response.isSuccess();
    }


    public Order buy(String marketName, double quantity, double price){
        return buyOrSellOrder(marketName,quantity,price, orderType.Buy);
    }


    public Order sell(String marketName, double quantity, double price){
        return buyOrSellOrder(marketName,quantity,price, orderType.Sell);
    }

    private Order buyOrSellOrder(String marketName, double quantity, double price, orderType type){
        Response response= null;
        if(type==orderType.Buy) response =client.buy(marketName, quantity, price);
        else if(type==orderType.Sell) response = client.sell(marketName,quantity, price);
        if(response!=null && JsonParserUtil.isAsuccess(response)){
            JsonObject jsonObject =JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
            String uuid = JsonParserUtil.getStringFromJsonObject(jsonObject, Constants.UUID);
            return getOrder(uuid);
        }

        return null;
    }

    public Order getOrder(String uuid){
        Response response = client.getOrder(uuid);
        if(JsonParserUtil.isAsuccess(response))return new Order(JsonParserUtil.getJsonObjectFromJsonString(response.getResult()));
        return null;
    }


    public JsonArray getAllOpenOrders(){
        Response response = client.getOpenOrders();
        if(JsonParserUtil.isAsuccess(response)) return JsonParserUtil.getJsonArrayFromJsonString(response.getResult());
        else return null;
    }



}
