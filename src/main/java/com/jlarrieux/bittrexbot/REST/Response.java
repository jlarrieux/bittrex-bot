package com.jlarrieux.bittrexbot.REST;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import com.jlarrieux.bittrexbot.simulation.TO.*;
import lombok.Data;
import lombok.extern.java.Log;



@Log
@Data
public class Response {

    //todo get rid of all those constructors
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

    public Response(MarketSummariesTO marketSummariesTO) {
        success = marketSummariesTO.getSuccess();
        message = marketSummariesTO.getMessage();
        result = new Gson().toJson(marketSummariesTO.getResult());
    }

    public Response(MarketOrderBookTO marketOrderBookTO) {
        success = marketOrderBookTO.getSuccess();
        message = marketOrderBookTO.getMessage();
        result = new Gson().toJson(marketOrderBookTO.getResult());
    }

    public Response(MarketSummaryTO marketNameTO) {
        success = marketNameTO.getSuccess();
        message = marketNameTO.getMessage();
        result = new Gson().toJson(marketNameTO.getResult());
    }

    public Response(BuyTO buyTO) {
        success = buyTO.getSuccess();
        message = buyTO.getMessage();
        result = new Gson().toJson(buyTO.getResult());
    }

    public Response(SellTO sellTO) {
        success = sellTO.getSuccess();
        message = sellTO.getMessage();
        result = new Gson().toJson(sellTO.getResult());
    }

    public Response(OrderTO orderTO) {
        success = orderTO.getSuccess();
        message = orderTO.getMessage();
        result = new Gson().toJson(orderTO.getResult());
    }

    public Response(BalanceTO balanceTO) {
        success = balanceTO.getSuccess();
        message = balanceTO.getMessage();
        result = new Gson().toJson(balanceTO.getResult());
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
