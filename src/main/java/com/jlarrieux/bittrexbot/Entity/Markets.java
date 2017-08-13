package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class Markets extends HashMap<String, Market> implements Container{


    @Override
    public Market add(JsonArray array, int i){
        Market market = new Market((JsonObject) array.get(i)) ;
        if(market.getBaseCurrency().equals("BTC")) {
            StringBuilder marketName = new StringBuilder(market.getMarketName());
            boolean doesNotContain = containsKey(market.getMarketName());
            if (!doesNotContain) put(marketName.toString(), market);
            Market m = get(marketName.toString());
            m.updatePrice(market.getLast());
            return m;
        }
        else return null;

    }

}
