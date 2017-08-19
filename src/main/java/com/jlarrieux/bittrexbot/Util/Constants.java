package com.jlarrieux.bittrexbot.Util;



import lombok.extern.java.Log;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;



@Log
public class Constants {


    public static final String RATE_CapitalFIRST = "Rate";
    public static final int DATA_WINDOW = 1440;
    public static final String BITTREX_BASE_URL="https://bittrex.com/api/v1.1/";
    public static final String BITTREX_BASE_URL2 = "https://bittrex.com/api/v2.0/";
    public static final String ENCRYPTION_ALGORITHM = "HmacSHA512";
    public static final String ORDER_LIMIT = "LIMIT";
    public static final String ORDER_MARKET = "MARKET";
    public static final String TRADE_BUY = "BUY";
    public static final String TRADE_SELL = "SELL";
    public static final String TIMEINEFFECT_GOOD_TIL_CANCELLED = "GOOD_TIL_CANCELLED";
    public static final String TIMEINEFFECT_IMMEDIATE_OR_CANCEL = "IMMEDIATE_OR_CANCEL";
    public static final String TIMEINEFFECT_FILL_OR_KILL = "FILL_OR_KILL";
    public static final String CONDITION_NONE = "NONE";
    public static final String CONDITION_GREATER_THAN = "GREATER_THAN";
    public static final String CONDITION_LESS_THAN = "LESS_THAN";
    public static final String CONDITION_STOP_LOSS_FIXED = "STOP_LOSS_FIXED";
    public static final String CONDITION_STOP_LOSS_PERCENTAGE = "STOP_LOSS_PERCENTAGE";
    public static final String API_VERSION = "2.0";
    public static final String METHOD_PUBLIC = "pub";
    public static final String METHOD_KEY = "key";
    public static final String MARKET = "market";
    public static final String MARKETS = "markets";
    public static final String CURRENCY = "currency";
    public static final String CURRENCIES = "currencies";
    public static final String BALANCE = "Balance";
    public static final String ORDERS = "orders";
    public static final String INITIAL_URL ="https://bittrex.com/api/";
    public static final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
    public static final String MARKET_CURRENCY = "MarketCurrencyLong";
    public static final String SUMMARY = "Summary";
    public static final String BASE_CURRENCY = "BaseCurrencyLong";
    public static final String MARKET_NAME = "MarketName";
    public static final String IS_ACTIVE = "IsActive";
    public static final String MIN_TRADE_SIZE = "MinTradeSize";
    public static final String HIGH = "High";
    public static final String LOW = "Low";
    public static final String LAST = "Last";
    public static final String BID = "Bid";
    public static final String ASK = "Ask";
    public static final String OPEN_BUY_ORDERS = "OpenBuyOrders";
    public static final String OPEN_SELL_ORDERS = "OpenSellOrders";
    public static final String VOLUME = "Volume";

    public static final String EXCHANGE = "Exchange";
    public static final String QUANTITY = "Quantity";
    public static final String AVAILABLE = "Available";
    private static final String JLARRIEUX_BITTREX_BASE_ADDRES ="http://bittrex.jlarrieux.com";
    public static final String BITTREX_JLARRIEUX_COM_MARKET_ID = JLARRIEUX_BITTREX_BASE_ADDRES+ "/market?id=";
    public static final String BITTREX_JLARRIEUX_COUNT = JLARRIEUX_BITTREX_BASE_ADDRES+"/count";
    public static final String MARKET_CURRENCY_SHORT = "marketCurrency";
    public static final String BASE_CURRENCY_SHORT = "baseCurrency";
    public static final String MARKET_NAME_SHORT = "marketName";
    public static final String ACTIVE = "active";
    public static final String MIN_TRADE_SIZE_SHORT = "minTradeSize";
    public static final String DATA_BOOK = "dataBook";
    public static final String SPREAD = "spread";
    public static final String OPEN_BUY_ORDERS_SHORT = "openBuyOrders";
    public static final String OPEN_SELL_ORDERS_SHORT = "openSellOrders";
    public static final String PUBLIC = "public";
    public static final String GETMARKETSUMMARIES = "/getmarketsummaries";
    public static final String ACCOUNT = "account/";
    public static final String GETBALANCES = "getbalances";
    public static final String APIKEY = "?apikey=";
    public static final String NONCE = "&nonce=";
    public static final String GETBALANCE = "getbalance";
    public static final String CURRENCY_URL = "&currency=";
    public static final String BUYLIMIT = "/buylimit";
    public static final String MARKET_URL = "&market=";
    public static final String QUANTITY_URL = "&quantity=";
    public static final String RATE = "&rate=";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String BTC = "btc";
    public static final int CURRENCY_PRECISION = 8;
    public static final String PUB = "pub/";
    public static final String GETMARKETORDERBOOK = "/getmarketorderbook?";
    public static final String ORDER_UUID = "OrderUuid";
    public static final String KEY = "key/";
    public static final String OPENED = "Opened";
    public static final String GETMARKETSUMMARY = "/getmarketsummary?";
    public static final String EQUAL = "=";
    public static final int SPACING = 15;
    public static final String BITTREX = "Bittrex";
    public static final String SIMULATOR = "Simulator";




    public static String upperCaseFirst(String value){
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }



    public static String buildDecimalFormatterPattern(int digitsAfterDecimal){
        StringBuilder builder = new StringBuilder("#,###,###,###,###,###,##0");
        if(digitsAfterDecimal>0){
            builder.append(".");
            for(int i=0; i<digitsAfterDecimal;i++)builder.append("#");
        }

        return builder.toString();
    }

    public static String format(double value,int d, String units){
        DecimalFormat format = new DecimalFormat(buildDecimalFormatterPattern(d));
        return format.format(value)+" "+units;
    }

    public static String addSpace(String currency){
        int l = currency.length();
        StringBuilder builder = new StringBuilder(currency);
        for(int i = 0; i< SPACING -l; i++) builder.append(" ");
        return builder.toString();

    }

    public static String addSpaceForDouble(double d, int digitsAfterDecimal, String units){
        return addSpace(format(d,digitsAfterDecimal, units));
    }


    public static String addSpaceForInt(int d, String units){
        return addSpace(String.valueOf(d+" "+ units));
    }

    public static  String addSpaceForLong(long d, String units){
        return addSpace(String.valueOf(d+" "+ units));
    }


    public static String buildBtcMarketName(String coin){
        return "BTC-"+coin;
    }

    public static String getListAsString(List arrayList){
        StringBuilder builder = new StringBuilder("[");
        for(Object o: arrayList) {
            if(builder.toString().length()>1)builder.append(",");
            builder.append(o.toString());
        }
        builder.append("]");
        return builder.toString();
    }











    public static void main(String[] args ){
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        log.info(String.format("Local time: %s\t\tutc time: %s", localDateTime.toString(), utc.toLocalDateTime().toString()));
    }






}
