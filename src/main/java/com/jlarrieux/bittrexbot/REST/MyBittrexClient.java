package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Properties.BittrexProperties;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.EncryptionUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Qualifier(Constants.BITTREX)
@Component
@Log
@Data
@Profile("!simulation")
public class MyBittrexClient extends RestClient implements ExchangeInterface {


    private String apikey;
    private String secret;



    
    public MyBittrexClient(BittrexProperties properties){
        if(properties!=null) {
            apikey = properties.getKey();
            secret = properties.getSecret();
        }
    }

    @Override
    protected Response getResponseBody(String jsonString) {
        return new Response(jsonString);
    }


    public Response getMarkets(){

        return getResponseBody(getJsonAsString(Constants.BITTREX_BASE_URL+Constants.PUBLIC+"/getmarkets"));
    }


    public Response getMarketSummaries(){
        return getResponseBody(getJsonAsString(Constants.BITTREX_BASE_URL2+ Constants.PUB +Constants.MARKETS + Constants.GETMARKETSUMMARIES));
    }

    public Response getMarketSummary(String marketName){
        return getResponseBody(getJsonAsString(Constants.BITTREX_BASE_URL+ Constants.PUBLIC+"/getticker?"+Constants.MARKET+Constants.EQUAL +marketName.toUpperCase()));
    }

    public Response getMarketOrderBook(String marketName){
        return getResponseBody(getJsonAsString(Constants.BITTREX_BASE_URL2+ Constants.PUB+Constants.MARKET+ Constants.GETMARKETORDERBOOK+Constants.MARKET_NAME.toLowerCase()+ Constants.EQUAL +marketName));
    }

    public Response getOpenOrders(){

        try {
            String url = Constants.BITTREX_BASE_URL+Constants.MARKET+ "/getopenorders"+Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce();
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public Response getBalance(String currencyAbbreviation){
        try {
            String url =Constants.BITTREX_BASE_URL+Constants.ACCOUNT+ Constants.GETBALANCE +Constants.APIKEY+apikey+Constants.NONCE+EncryptionUtil.generateNonce()+ Constants.CURRENCY_URL +currencyAbbreviation;

            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Response getBalances(){
        try {

             String url = Constants.BITTREX_BASE_URL+ Constants.ACCOUNT + Constants.GETBALANCES + Constants.APIKEY +apikey+ Constants.NONCE + EncryptionUtil.generateNonce();
            ClientResponse response = buildPrivateUrl(url);
//            log.info(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }


    public Response cancelOrder(String uuid){
        try {
            String url =Constants.BITTREX_BASE_URL2+ Constants.KEY +Constants.MARKET+"/tradecancel"+Constants.APIKEY+apikey+Constants.NONCE+EncryptionUtil.generateNonce()+"&orderid="+uuid;
//            log.info("url: "+ url);
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Response buy(String marketName, double quantity, double price){

        try {
            log.info(String.format("marketname: %s\tquantity: %f\tunitPrice: %f", marketName, quantity,price));
            String url = Constants.BITTREX_BASE_URL+ Constants.MARKET+ Constants.BUYLIMIT +Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce()+ Constants.MARKET_URL +marketName+ Constants.QUANTITY_URL +String.valueOf( quantity)+ Constants.RATE +String.valueOf( price);
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Response sell(String marketName, double quantity, double price){

        try {
            String url = Constants.BITTREX_BASE_URL+ Constants.MARKET+ "/selllimit"+Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce()+ Constants.MARKET_URL +marketName+ Constants.QUANTITY_URL +quantity+ Constants.RATE +price;
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Response getOrderHistory(String marketName) {


        try {
            String url = Constants.BITTREX_BASE_URL+Constants.ACCOUNT+"getorderhistory"+Constants.APIKEY+apikey+Constants.NONCE+ EncryptionUtil.generateNonce()+"&"+Constants.MARKET+Constants.EQUAL+marketName;
            log.info(url);
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }



    @Override
    public Response getOrder(String uuid) {
        try {
            String url = Constants.BITTREX_BASE_URL+Constants.ACCOUNT+"getorder"+Constants.APIKEY+apikey+Constants.NONCE+EncryptionUtil.generateNonce()+"&uuid"+Constants.EQUAL+uuid;
            log.info(url);
            ClientResponse response = buildPrivateUrl(url);
            return getResponseBody(response.getEntity(String.class));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } return null;
    }



    private ClientResponse buildPrivateUrl(String url){
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        try {
            StringBuilder builder = new StringBuilder(EncryptionUtil.calculateHash(secret,url));
            log.info("apisign: "+builder.toString()+"\nURL: "+ url);
            return webResource.header("apisign", builder.toString()).get(ClientResponse.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }






    public static void main(String[] args){
        MyBittrexClient client = new MyBittrexClient(null);
        client.setSecret("faf2bf0bb20b4b5db3763868e814c190");
        client.setApikey("9893fcf9b4b848f79cddf3d9d246a9bf");
        String marketName = "BTC-HMQ";
        Response response = client.getOrder("6fa336ac-f4e2-45b2-93b3-a30e51f39f31");
        log.info(response.toString());
////        Response response = new Response(client.getOrder("6f8d7ab7-ae71-4417-a983-5e692ddb9e0d"));
//        if(JsonParserUtil.isAsuccess(response)){
//            log.info(response.getResult());
//            Order order = new Order(JsonParserUtil.getJsonObjectFromJsonString(response.getResult()));
//            log.info("\n\n"+order.toString());
//        }
//
//        else log.severe(response.getMessage());
////        JsonObject jsonObject = JsonParserUtil.getJsonObjectFromJsonString(response.getResult());
////        double ask = JsonParserUtil.getDoubleFromJsonObject(jsonObject,Constants.ASK);
////        log.info(client.buy(marketName,20,ask));
    }




}
