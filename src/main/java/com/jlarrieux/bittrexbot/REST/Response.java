package com.jlarrieux.bittrexbot.REST;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import com.jlarrieux.bittrexbot.simulation.TO.MarketOrderBookTO;
import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;
import lombok.Data;
import lombok.extern.java.Log;



@Log
@Data
public class Response {


    private boolean success;
    private int responseCode;
    private String result;
    private String message;

    private Response(boolean success, int responseCode, String result, String message) {

        this.success = success;
        this.responseCode = responseCode;
        this.result = result;
        this.message = message;
    }

    public Response(boolean success, String result, String message) {

        this.success = success;
        this.result = result;
        this.message = message;
    }

    public Response(String jsonString){
//        log.info("inside response: " +jsonString+"\n");
        JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(jsonString);
        success = JsonParserUtil.getBooleanFromJsonObject(object, Constants.SUCCESS);
        message = JsonParserUtil.getStringFromJsonObject(object, Constants.MESSAGE);
        result = object.get(Constants.RESULT).toString();
    }

    public Response(ResponseTO responseTO) {
        success = responseTO.getSuccess();
        message = responseTO.getMessage();
        result = new Gson().toJson(responseTO.getResult());
    }

    public Response(MarketOrderBookTO marketOrderBookTO) {
        success = marketOrderBookTO.getSuccess();
        message = marketOrderBookTO.getMessage();
        result = new Gson().toJson(marketOrderBookTO.getResult());
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
