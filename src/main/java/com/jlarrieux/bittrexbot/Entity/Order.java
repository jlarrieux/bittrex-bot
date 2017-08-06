package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Data
@Component
public class Order {

    private LocalDate dateCreated;
    private String marketName;
    private double quantity, buyPrice, lastPrice;
    private double profitAndLoss = (lastPrice-buyPrice);
    private double profitAndLossPercent = profitAndLoss/buyPrice;

    public Order(){

    }

    public Order(JsonObject object){
        buildOrder(object);
    }



    private void buildOrder(JsonObject object) {
        marketName = JsonParserUtil.getStringFromJsonObject(object,"Exchange");
        quantity = JsonParserUtil.getDoubleFromJsonObject(object,"Quantity");


    }

}
