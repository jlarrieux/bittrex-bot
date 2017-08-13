package com.jlarrieux.bittrexbot.Entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Orders extends HashMap<String, Order> implements Container{

    @Override
    public Order add(JsonArray array, int i){
        return new Order((JsonObject) array.get(i));
    }
}
