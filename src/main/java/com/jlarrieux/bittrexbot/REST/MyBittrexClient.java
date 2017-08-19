package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Properties.BittrexProperties;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.EncryptionUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Qualifier(Constants.BITTREX)
@Component
@Log
public class MyBittrexClient extends RestClient implements ExchangeInterface {


    private String apikey;
    private String secret;
    
    public MyBittrexClient(BittrexProperties properties){
        apikey = properties.getKey();
        secret = properties.getSecret();
    }

    @Override
    protected Response getResponseBody(String url) {
        return null;
    }


    public String getMarkets(){

        return getJsonAsString(Constants.BITTREX_BASE_URL+Constants.PUBLIC+"/getmarkets");
    }


    public String getMarketSummaries(){
        return getJsonAsString(Constants.BITTREX_BASE_URL2+ Constants.PUB +Constants.MARKETS + Constants.GETMARKETSUMMARIES);
    }

    public String getMarketSummary(String marketName){
        return getJsonAsString(Constants.BITTREX_BASE_URL+ Constants.PUBLIC+"/getticker?"+Constants.MARKET+Constants.EQUAL +marketName.toUpperCase());
    }

    public String getMarketOrderBook(String marketName){
        return getJsonAsString(Constants.BITTREX_BASE_URL2+ Constants.PUB+Constants.MARKET+ Constants.GETMARKETORDERBOOK+Constants.MARKET_NAME.toLowerCase()+ Constants.EQUAL +marketName);
    }

    public String getOpenOrders(){

        try {
            String url = Constants.BITTREX_BASE_URL+Constants.MARKET+ "/getopenorders"+Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce();
            ClientResponse response = buildPrivateUrl(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public String getBalance(String currencyAbbreviation){
        try {
            String url =Constants.BITTREX_BASE_URL+Constants.ACCOUNT+ Constants.GETBALANCE +Constants.APIKEY+apikey+Constants.NONCE+EncryptionUtil.generateNonce()+ Constants.CURRENCY_URL +currencyAbbreviation;

            ClientResponse response = buildPrivateUrl(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getBalances(){
        try {

             String url = Constants.BITTREX_BASE_URL+ Constants.ACCOUNT + Constants.GETBALANCES + Constants.APIKEY +apikey+ Constants.NONCE + EncryptionUtil.generateNonce();
            ClientResponse response = buildPrivateUrl(url);
//            log.info(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }


    public String cancelOrder(String uuid){
        try {
            String url =Constants.BITTREX_BASE_URL2+ Constants.KEY +Constants.MARKET+"/tradecancel"+Constants.APIKEY+apikey+Constants.NONCE+EncryptionUtil.generateNonce()+"&orderid="+uuid;
//            log.info("url: "+ url);
            ClientResponse response = buildPrivateUrl(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String buy(String marketName, double quantity, double price){

        try {
            String url = Constants.BITTREX_BASE_URL+ Constants.MARKET+ Constants.BUYLIMIT +Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce()+ Constants.MARKET_URL +marketName+ Constants.QUANTITY_URL +quantity+ Constants.RATE +price;
            ClientResponse response = buildPrivateUrl(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String sell(String marketName, double quantity, double price){

        try {
            String url = Constants.BITTREX_BASE_URL+ Constants.MARKET+ "/selllimit"+Constants.APIKEY+apikey+Constants.NONCE + EncryptionUtil.generateNonce()+ Constants.MARKET_URL +marketName+ Constants.QUANTITY_URL +quantity+ Constants.RATE +price;
            ClientResponse response = buildPrivateUrl(url);
            return response.getEntity(String.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ClientResponse buildPrivateUrl(String url){
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        try {
            StringBuilder builder = new StringBuilder(EncryptionUtil.calculateHash(secret,url));
//            log.info("apisign: "+builder.toString());
            return webResource.header("apisign", builder.toString()).get(ClientResponse.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }






}
