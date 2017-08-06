package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class Orders extends HashMap<String, Order> {

    public Order add(JsonArray array, int i){
        Order order = new Order((JsonObject) array.get(i));

        return order;
    }


}
