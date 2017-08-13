package com.jlarrieux.bittrexbot.Util;



public class Constants {

    private static final int window = 3178;
    public static final String URI ="https://bittrex.com/api/v1.1/market/getopenorders?apikey=";
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
    public static final int BOLLINGER_WINDOW = window;
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
    public static int RSI_WINDOW =window;



    public static String upperCaseFirst(String value){
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }

    public static void main(String[] args ){
        double d1=1.865815770698525E-5;
        double d2 = 1.809488229301446E-5;
        System.out.println(Double.compare(d1,d2));
    }


}
