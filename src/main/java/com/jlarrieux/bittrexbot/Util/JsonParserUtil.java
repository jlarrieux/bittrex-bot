package com.jlarrieux.bittrexbot.Util;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jlarrieux.bittrexbot.REST.Response;




public class JsonParserUtil {

    public static boolean getBooleanFromJsonObject(JsonObject jsonObject, String key){
        return jsonObject.getAsJsonPrimitive(key).getAsBoolean();
    }

    public static double getDoubleFromJsonObject(JsonObject jsonObject, String key) {

        return jsonObject.getAsJsonPrimitive(key).getAsDouble();
    }



    public static int getIntFromJsonObject(JsonObject jsonObject, String key) {
        return jsonObject.getAsJsonPrimitive(key).getAsInt();
    }

    public static long getLongFromJsonObject(JsonObject jsonObject, String key){
        return jsonObject.getAsJsonPrimitive(key).getAsLong();
    }



    public static JsonObject getJsonObjectFromJsonString(String json) {
//        log.info("Maybe null: "+json);
        return getJsonElementFromString(json).getAsJsonObject();
    }



    public static JsonArray getJsonArrayFromJsonString(String json){
        return getJsonElementFromString(json).getAsJsonArray();
    }



    public static JsonElement getJsonElementFromString(String json){

        return new JsonParser().parse(json);
    }



    public static String getStringFromJsonObject(JsonObject jsonObject, String key) {
        return jsonObject.getAsJsonPrimitive(key).getAsString();
    }



    public static boolean isAsuccess(Response response){
        boolean isGood = response.isSuccess();
        if(!response.isSuccess()){
//            log.severe(String.format("Response unsuccessfull!%nwith Code: %d%nAnd Message: %s", response.getResponseCode(), response.getMessage()));
//            log.info(response.toString());
        }
        return isGood;
    }
}
