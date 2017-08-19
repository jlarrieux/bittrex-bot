package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Log
@Component
public class Markets extends HashMap<String, Market> implements Container{


    @Override
    public Market add(JsonArray array, int i){
        Market market = new Market((JsonObject) array.get(i)) ;
        if(market.getBaseCurrency().equals("Bitcoin")) return add(market);
        else return null;

    }


    public Market add(Market market){
        StringBuilder marketName = new StringBuilder(market.getMarketName());
        boolean doesContain = containsKey(market.getMarketName());
        if (!doesContain) put(marketName.toString(), market);
        Market m = get(marketName.toString());
        m.update(market);
        return m;
    }

}
