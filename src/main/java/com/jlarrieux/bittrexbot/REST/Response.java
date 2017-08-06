package com.jlarrieux.bittrexbot.REST;



import lombok.Data;



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



}
