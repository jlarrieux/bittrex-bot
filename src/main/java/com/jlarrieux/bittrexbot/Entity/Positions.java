package com.jlarrieux.bittrexbot.Entity;



import com.google.common.math.DoubleMath;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class Positions extends HashMap<String, Position>  implements Container{


    @Override
    public Position add(JsonArray array, int i){
        Position position = new Position((JsonObject) array.get(i));
        if(!DoubleMath.fuzzyEquals(position.getQuantity(),0,0.0000000009)){
            put(position.getCurrency(), position);
            return position;
        }

        return null;

    }






}
