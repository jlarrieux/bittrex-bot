package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Properties.BittrexProperties;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.EncryptionUtil;
import lombok.extern.java.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Log
@Component
public class BittrexRestClient extends RestClient {


    private String apikey;
    private String secret;


    @Autowired
    public BittrexRestClient(BittrexProperties properties){
        apikey = properties.getKey();
        secret = properties.getSecret();
    }










    public Response getMarketSummaries() { // Returns a 24-hour summary of all markets

        return getResponse(Constants.METHOD_PUBLIC, Constants.MARKETS, "getmarketsummaries");
    }

    public Response getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

        return getResponse(Constants.METHOD_PUBLIC, Constants.CURRENCIES, "getcurrencies");
    }

    public Response getWalletHealth() { // Returns wallet health

        return getResponse(Constants.METHOD_PUBLIC, Constants.CURRENCIES, "getwallethealth");
    }

    public Response getBalanceDistribution(String currency) { // Returns the balanceContainer distribution for a specific currency

        return getResponse(Constants.METHOD_PUBLIC, Constants.CURRENCY, "getbalancedistribution", returnCorrectMap("currencyname", currency));
    }

    public Response getMarketSummary(String market) { // Returns a 24-hour summar for a specific market

        return getResponse(Constants.METHOD_PUBLIC, Constants.MARKET, "getmarketsummary", returnCorrectMap("marketname", market));
    }

    public Response getMarketOrderBook(String market) { // Returns the orderbook for a specific market

        return getResponse(Constants.METHOD_PUBLIC, Constants.MARKET, "getmarketorderbook", returnCorrectMap("marketname", market));
    }

    public Response getMarketHistory(String market) { // Returns latest trades that occurred for a specific market

        return getResponse(Constants.METHOD_PUBLIC, Constants.MARKET, "getmarkethistory", returnCorrectMap("marketname", market));
    }

    public Response getMarkets() { // Returns all markets with their metadata

        return getResponse(Constants.METHOD_PUBLIC, Constants.MARKETS, "getmarkets");
    }

    public Response getOrder(String orderId) { // Returns information about a specific order (by UUID)

        return getResponse(Constants.METHOD_KEY, Constants.ORDERS, "getorder", returnCorrectMap("orderid", orderId));
    }

    public Response getOpenOrders() { // Returns all your currently open orders

        return getResponse(Constants.METHOD_KEY, Constants.ORDERS, "getopenorders");
    }

    public Response getOrderHistory() { // Returns all of your order history

        return getResponse(Constants.METHOD_KEY, Constants.ORDERS, "getorderhistory");
    }

    public Response cancelOrder(String orderId) { // Cancels a specific order based on its order's UUID.

        return getResponse(Constants.METHOD_KEY, Constants.MARKET, "tradecancel", returnCorrectMap("orderid", orderId));
    }

    public Response getOpenOrders(String market) { // Returns your currently open orders in a specific market

        return getResponse(Constants.METHOD_KEY, Constants.MARKET, "getopenorders", returnCorrectMap("marketname", market));
    }

    public Response getOrderHistory(String market) { // Returns your order history in a specific market

        return getResponse(Constants.METHOD_KEY, Constants.MARKET, "getorderhistory", returnCorrectMap("marketname", market));
    }

    public Response getBalances() { // Returns all current balances

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getbalances");
    }

    public Response getBalance(String currency) { // Returns the balanceContainer of a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getbalance", returnCorrectMap("currencyname", currency));
    }

    public Response getPendingWithdrawals(String currency) { // Returns pending withdrawals for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getpendingwithdrawals", returnCorrectMap("currencyname", currency));
    }

    public Response getPendingWithdrawals() { // Returns all pending withdrawals

        return getPendingWithdrawals("");
    }

    public Response getWithdrawalHistory(String currency) { // Returns your withdrawal history for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getwithdrawalhistory", returnCorrectMap("currencyname", currency));
    }

    public Response getWithdrawalHistory() { // Returns your whole withdrawal history

        return getWithdrawalHistory("");
    }

    public Response getPendingDeposits(String currency) { // Returns pending deposits for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getpendingdeposits", returnCorrectMap("currencyname", currency));
    }

    public Response getPendingDeposits() { // Returns pending deposits for a specific currency

        return getPendingDeposits("");
    }

    public Response getDepositHistory(String currency) { // Returns your deposit history for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getdeposithistory", returnCorrectMap("currencyname", currency));
    }

    public Response getDepositHistory() { // Returns your whole deposit history

        return getDepositHistory("");
    }

    public Response getDepositAddress(String currency) { // Returns your deposit address for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "getdepositaddress", returnCorrectMap("currencyname", currency));
    }

    public Response generateDepositAddress(String currency) { // Generates a new deposit address for a specific currency

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "generatedepositaddress", returnCorrectMap("currencyname", currency));
    }

    public Response withdraw(String currency, String amount, String address) { // Withdraws a specific amount of a certain currency to the specified address

        return getResponse(Constants.METHOD_KEY, Constants.BALANCE, "withdrawcurrency", returnCorrectMap("currencyname", currency, "quantity", amount, "address", address));
    }



    /**
     *
     * *
     * @param tradeType
     * @param market
     * @param orderType
     * @param quantity
     * @param rate
     * @param timeInEffect
     * @param conditionType
     * @param target
     * @return
     */
    public Response placeOrder(String tradeType, String market, String orderType, String quantity, String rate, String timeInEffect, String conditionType, String target) { // Places a buy/sell order with these specific conditions (target only required if a condition is in place)

        String method = null;

        if(tradeType.equals(Constants.TRADE_BUY))     method = "tradebuy";
        else if(tradeType.equals(Constants.TRADE_SELL))      method = "tradesell";

        if(conditionType.equals(Constants.CONDITION_NONE)) // Ignore target if the condition is none.

            return placeNonConditionalOrder(tradeType, market, orderType, quantity, rate, timeInEffect);

        return getResponse(Constants.METHOD_KEY, Constants.MARKET, method, returnCorrectMap("marketname", market, "ordertype", orderType, "quantity", quantity, "rate", rate, "timeineffect", timeInEffect, "conditiontype", conditionType, "target", target));
    }




    public Response placeNonConditionalOrder(String tradeType, String market, String orderType, String quantity, String rate, String timeInEffect) { // Used for non-conditional orders

        return placeOrder(tradeType, market, orderType, quantity, rate, timeInEffect, Constants.CONDITION_NONE, "0");
    }

    public void setSecret(String secret) {

        this.secret = secret;
    }

    public void setKey(String apikey) {

        this.apikey = apikey;
    }

    private HashMap<String, String> returnCorrectMap(String...parameters) { // Handles the exception of the generateHashMapFromStringList() method gracefully as to not have an excess of try-catch statements

        HashMap<String, String> map = null;

        try {

            map = generateHashMapFromStringList(parameters);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return map;
    }

    private HashMap<String, String> generateHashMapFromStringList(String...strings) throws Exception { // Method to easily create a HashMap from a list of Strings

        if(strings.length % 2 != 0)

            throw Constants.InvalidStringListException;

        HashMap<String, String> map = new HashMap<String, String>();

        for(int i = 0; i < strings.length; i += 2) // Each key will be i, with the following becoming its value

            map.put(strings[i], strings[i + 1]);

        return map;
    }

    private Response getResponse(String type, String methodGroup, String method) {

        return getResponse(type, methodGroup, method, new HashMap<String, String>());
    }

    private Response getResponse(String type, String methodGroup, String method, HashMap<String, String> parameters) {

        return getResponseBody(generateUrl(type, methodGroup, method, parameters));
    }

    private String generateUrl(String type, String methodGroup, String method, HashMap<String, String> parameters) {

        String url = Constants.INITIAL_URL;

        url += "v" + Constants.API_VERSION + "/";
        url += type + "/";
        url += methodGroup + "/";
        url += method;

        url += generateUrlParameters(parameters);

        return url;
    }

    private String generateUrlParameters(HashMap<String, String> parameters) { // Returns a String with the key-value pairs formatted for URL

        String urlAttachment = "?";

        Object[] keys = parameters.keySet().toArray();

        for(Object key : keys)

            urlAttachment += key.toString() + "=" + parameters.get(key) + "&";

        return urlAttachment;
    }

    @Override
    protected Response getResponseBody(String url)  {

        Response response = null;
        boolean publicRequest = true;

        if(!url.substring(url.indexOf("v" + Constants.API_VERSION)).contains("/" + Constants.METHOD_PUBLIC + "/")) { // Only attach apikey & nonce if it is not a public method

            try {
                url += "apikey=" + apikey + "&nonce=" + EncryptionUtil.generateNonce();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            publicRequest = false;
//            log.info("red%n%"+getJsonAsString(url));
        }

        try {

            log.info(String.format("Printing url:"+ url));
            org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();

            HttpGet request = new HttpGet(url);
//            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//            if(!publicRequest)
                request.addHeader("apisign", EncryptionUtil.calculateHash(secret, url)); // Attaches signature as a header

            HttpResponse httpResponse = client.execute(request);

            int responseCode = httpResponse.getStatusLine().getStatusCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer resultBuffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null)

                resultBuffer.append(line);

            response = createResposeFromUrlResponse(resultBuffer.toString());
            response.setResponseCode(responseCode);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return response;
    }





}
