package com.jlarrieux.bittrexbot.REST;



public interface ExchangeInterface {

    Response getMarkets();
    Response getMarketSummary(String marketName);
    Response getMarketOrderBook(String marketName);
    Response getMarketSummaries();
    Response getOpenOrders();
    Response getBalance(String currency);
    Response getBalances();
    Response cancelOrder(String id);
    Response buy(String marketName, double quantity, double price);
    Response sell(String marketName, double quantity, double price);
    Response getOrderHistory(String marketName);
    Response getOrder(String uuid);


}
