package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;



@Log
@Component
public class PositionManager {


    private MyBittrexClient client;
    private Positions positionBooks = new Positions();
    private double fee;



    public PositionManager(){

    }


    @Autowired
    public PositionManager(MyBittrexClient client, Positions positions, BittrexProperties bittrexProperties){

        fee = bittrexProperties.getFee();
        this.client = client;
        this.positionBooks = positions;

    }




    public Positions getPositionBooks(){
        return positionBooks;
    }




    public void add(Position p){
        Position p2 ;
        if(positionBooks.containsKey(p.getCurrency())){
            p2 = positionBooks.get(p.getCurrency());
            p2.update(p.getQuantity(), p.getAveragePurchasedPrice());
        }
        else p2 = p;
        positionBooks.put(p2.getCurrency(), p2);
    }

    public void remove(String key){
        positionBooks.remove(key);
    }





    public double calculateTotal(double price){
        double transactionFee = price *fee/100;
        return  price + transactionFee;
    }


    private JsonObject getLastPrice(String marketname){
        Response response = client.getMarketSummary(marketname);
        if(JsonParserUtil.isAsuccess(response)&& response.getResult().length()>5){
            return  JsonParserUtil.getJsonObjectFromJsonString(response.getResult());

        }
        return null;
    }

    private JsonObject getMarketOrderFirstObject(String marketName){
        Response r = client.getMarketOrderBook(marketName);
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



    public List<Position> getAllOpenPositions(){
        List<Position> allPositions = new ArrayList<>();
        Response r =client.getBalances();
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



    public boolean contains(String key){
        return positionBooks.containsKey(key);
    }

    public double getTotalPricePaid(String key){
        return positionBooks.get(key).getTotal();
    }


    public double getQuantityOwn(String currency){
        if(positionBooks.containsKey(currency)) return positionBooks.get(currency).getQuantity();
        else return 0;
    }



    public static void main(String[] args){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime utc = LocalDateTime.now(Clock.systemUTC());
        String s = "2017-08-27T04:49:17.36";
        LocalDateTime localDateTime1 = LocalDateTime.parse(s);
        ZonedDateTime zonedDateTime = localDateTime1.atZone(ZoneId.of("UTC"));

        log.info(String.format("Local: %s\nUTC: %s\nlocal from string: %s\nUtc2: %s", localDateTime.toString(),utc.toString(), localDateTime1.toString(), zonedDateTime.toString()));
    }


}
