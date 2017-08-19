package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.Properties.BittrexProperties;
import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Log
@Component
public class PositionManager {


    private MyBittrexClient client;
    private Positions positionBooks;
    private double fee;






    @Autowired
    public PositionManager(MyBittrexClient client, Positions positions, BittrexProperties bittrexProperties){

        fee = bittrexProperties.getFee();
        this.client = client;
        this.positionBooks = positions;

    }




    public Positions getPositionBooks(){
        return positionBooks;
    }

    public double buy(Market market, double quantity, double price){
        client.buy(market.getMarketName(),quantity,price);
        double total = calculateTotal(price*quantity);
        Position p= null;
        if(positionBooks.containsKey(market.getMarketName())){
            p = positionBooks.get(market.getMarketName());
            p.update(quantity, price);
        }
        else {
            p = new Position(quantity, price, market.getMarketCurrency());
            p.setMarketName(market.getMarketName());
        }
        positionBooks.put(market.getMarketName(),p);

        return total;
    }

    public double sell( Position p){

        JsonObject jsonObject = getLastPrice(p.getMarketName());
        if(jsonObject!=null && !jsonObject.get(Constants.BID).toString().equals("null")){
//            log.info("before maybe null: "+p.getMarketName()+"\t bid: "+ jsonObject.get(Constants.BID));
            double bid = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.BID);
            double last = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.LAST);
            double ask = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.ASK);
            double quantity = p.getQuantity();
            double price = bid;
            Response response = new Response(client.sell(p.getMarketName(), quantity, bid));
            if(JsonParserUtil.isAsuccess(response)){
                double total = quantity * price;
                log.info(String.format("Selling %f units of %s at %f btc with bid: %f and ask: %f and last: %f", quantity, p.getCurrency(), price,bid, ask, last ));
                return total;
            }
        }


        return 0;
    }



    public double calculateTotal(double price){
        double transactionFee = price *fee/100;
        return  price + transactionFee;
    }


    private JsonObject getLastPrice(String marketname){
        Response response = new Response(client.getMarketSummary(marketname));
        if(JsonParserUtil.isAsuccess(response)&& response.getResult().length()>5){
            return  JsonParserUtil.getJsonObjectFromJsonString(response.getResult());

        }
        return null;
    }

    private JsonObject getMarketOrderFirstObject(String marketName){
        Response r = new Response(client.getMarketOrderBook(marketName));
//        log.info(String.valueOf(r.getResult().length()));
        if(r.getResult().length()>50) {
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(r.getResult());
            JsonArray array = object.getAsJsonArray("buy");
            return (JsonObject) array.get(0);
        }
        return null;
    }

    private double calculateQuantity(JsonObject jsonObject){
        return JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.QUANTITY);

    }

    private double calculatePrice(JsonObject jsonObject){
        return JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.RATE_CapitalFIRST);
    }

    public void sellAll(){
       List<Position> allPosition = getAllOpenPositions();
       if(allPosition!=null) sellEach(allPosition);
    }

    private void sellEach(List<Position> allposition){
        for(Position p: allposition){
            if(shouldSell(p)) sell(p);
        }
    }

    public List<Position> getAllOpenPositions(){
        List<Position> allPositions = new ArrayList<>();
        Response r = new Response(client.getBalances());
        if(JsonParserUtil.isAsuccess(r)){
            JsonArray array = JsonParserUtil.getJsonArrayFromJsonString(r.getResult());
            for(int i=0 ;i<array.size();i++){
                Position p = new Position();
                p.alternatBuild((JsonObject) array.get(i));
                allPositions.add(p);
            }
            return allPositions;
        }

        return null;
    }

    private boolean shouldSell(Position p){
//        String currency = p.getCurrency();
//        log.info(p.getMarketName());
//        Market m= marketManager.getMarketBooks().get(p.getMarketName());
//        Double min = m.getMinTradeSize();
//        boolean minTradeSatisfied = true;
//        if(min!=null) minTradeSatisfied = min<=p.getQuantity();

        return    true;//m.getMinTradeSize()<=p.getQuantity();//!currency.equals("STORJ") && !currency.equals("OMG");// &&
    }


    public boolean contains(Position p){
        return positionBooks.containsKey(p.getMarketName());
    }




}
