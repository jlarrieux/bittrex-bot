package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Data
@Component
public class Order {

    private String marketName;
    private double quantity;

    public Order(){

    }


    public Order(JsonObject object){
        buildOrder(object);
    }



    private void buildOrder(JsonObject object){
        marketName = JsonParserUtil.getStringFromJsonObject(object, Constants.EXCHANGE);
        quantity = JsonParserUtil.getDoubleFromJsonObject(object, Constants.QUANTITY);
    }
}
