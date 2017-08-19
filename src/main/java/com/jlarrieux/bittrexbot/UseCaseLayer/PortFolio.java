package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager;
import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Log
@Component
public class PortFolio {



    private MyBittrexClient client;
    private Positions positionBooks = new Positions();
    private double total;
    private PositionManager positionManager;


    @Autowired
    public PortFolio(MyBittrexClient client, PositionManager manager){
        this.client = client;
        this.positionManager = manager;

    }

    public double getCurrentPortFolioValue(){

        total=0;
        buildPositionBooks();
        return  total;
    }


    private void buildPositionBooks(){
        positionBooks.clear();
        List<Position> allPostions = positionManager.getAllOpenPositions();
        if(allPostions!=null){
            for(Position p: allPostions) positionBooks.put(p.getCurrency(), p);
            calculateTotal();
        }

    }



    private void calculateTotal(){
//        log.info("Starting portfolio calculation...");
        int i=1;
        for(String key: positionBooks.keySet()){
//            log.info(String.format("%d out of %d", i, positionBooks.size()));
            Position p = positionBooks.get(key);
            lowLevelCalculation(p);
            i++;
        }
//        log.info("Done calculating portfolio");
    }


    private void lowLevelCalculation(Position p){
        JsonObject jsonObject = getMarketSummary(p.getMarketName());
        StringBuilder builder;
        if(jsonObject!=null ){
                if(!jsonObject.get(Constants.BID).toString().equals("null")) {
                    double last = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.LAST);
                    double bid = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.BID);
                    double ask = JsonParserUtil.getDoubleFromJsonObject(jsonObject, Constants.ASK);

                    double current = p.getQuantity() * last;
                    total += current;
//                log.info(String.format("Coin: %s\t  with quantity: %f\t current : %f\t and last: %f \t and bid: %f \t and ask: %f \toverall total: %f", Constants.addSpace(p.getCurrency()), p.getQuantity(), current, last, bid, ask, total));
                }
        }
        else{
//            double balance = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.BALANCE);
            total+=p.getQuantity();
//            log.info(String.format("Coin: %s\t with quantity: %f\t current: \tand overall total: %f", Constants.addSpace(p.getCurrency()), p.getQuantity(),total));
        }


    }


    private JsonArray getBuyBook(String marketName){
        Response response = new Response(client.getMarketOrderBook(marketName));
        if(response.getResult().length()>50){
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
            return object.getAsJsonArray("buy");
        }

        return null;
    }

    private JsonObject getMarketSummary(String marketName){
        Response response = new Response(client.getMarketSummary(marketName));
        if(JsonParserUtil.isAsuccess(response)&& !response.getResult().contains("null"))return JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
        return null;
    }




}