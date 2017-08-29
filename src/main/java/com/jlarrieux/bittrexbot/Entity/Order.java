package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;



@Data
@Component
public class Order {


    private String marketName;
    private double quantity, quantityRemaining, limit;
    private String orderUuid;
    private LocalDateTime opened;
    private Boolean isOpen=null, cancelIniated = null;
    private orderType type;

    public enum orderType{
        LIMIT_BUY, LIMIT_SELL
    }

    public Order(){

    }


    public Order(JsonObject object){
        buildOrder(object);
    }



    private void buildOrder(JsonObject object){
        marketName = JsonParserUtil.getStringFromJsonObject(object, Constants.EXCHANGE);
        quantity = JsonParserUtil.getDoubleFromJsonObject(object, Constants.QUANTITY);
        orderUuid = JsonParserUtil.getStringFromJsonObject(object, Constants.ORDER_UUID);
        opened = LocalDateTime.parse(JsonParserUtil.getStringFromJsonObject(object, Constants.OPENED));
        quantityRemaining = JsonParserUtil.getDoubleFromJsonObject(object, Constants.QUANTITY_REMAINING);
        limit = JsonParserUtil.getDoubleFromJsonObject(object, Constants.LIMIT);
        if(object.has(Constants.IS_OPEN)) isOpen = JsonParserUtil.getBooleanFromJsonObject(object, Constants.IS_OPEN);
        if(object.has(Constants.CANCEL_INITIATED)) cancelIniated = JsonParserUtil.getBooleanFromJsonObject(object,Constants.CANCEL_INITIATED);
        if(object.has(Constants.TYPE)) type = orderType.valueOf(JsonParserUtil.getStringFromJsonObject(object,Constants.TYPE));
        else if(object.has(Constants.ORDER_TYPE)) type = orderType.valueOf(JsonParserUtil.getStringFromJsonObject(object,Constants.ORDER_TYPE));

    }
}
