package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;



@Data
@Component
public class Order {


    @Getter @Setter
    private String marketName;
    @Getter @Setter
    private double quantity, quantityRemaining, limit;

    @Getter @Setter
    private String orderUuid;

    @Getter @Setter
    private LocalDateTime opened;

    @Getter @Setter
    private Boolean isOpen=null, cancelIniated = null;

    @Getter @Setter
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
        if(object.has(Constants.EXCHANGE)) marketName = JsonParserUtil.getStringFromJsonObject(object, Constants.EXCHANGE);
        if(object.has(Constants.QUANTITY)) quantity = JsonParserUtil.getDoubleFromJsonObject(object, Constants.QUANTITY);
        if(object.has(Constants.ORDER_UUID)) orderUuid = JsonParserUtil.getStringFromJsonObject(object, Constants.ORDER_UUID);
        if(object.has(Constants.OPENED)) opened = LocalDateTime.parse(JsonParserUtil.getStringFromJsonObject(object, Constants.OPENED));
        if(object.has(Constants.QUANTITY_REMAINING)) quantityRemaining = JsonParserUtil.getDoubleFromJsonObject(object, Constants.QUANTITY_REMAINING);
        if(object.has(Constants.LIMIT)) limit = JsonParserUtil.getDoubleFromJsonObject(object, Constants.LIMIT);
        if(object.has(Constants.IS_OPEN)) isOpen = JsonParserUtil.getBooleanFromJsonObject(object, Constants.IS_OPEN);
        if(object.has(Constants.CANCEL_INITIATED)) cancelIniated = JsonParserUtil.getBooleanFromJsonObject(object,Constants.CANCEL_INITIATED);
        if(object.has(Constants.TYPE)) type = orderType.valueOf(JsonParserUtil.getStringFromJsonObject(object,Constants.TYPE));
        else if(object.has(Constants.ORDER_TYPE)) type = orderType.valueOf(JsonParserUtil.getStringFromJsonObject(object,Constants.ORDER_TYPE));

    }
}
