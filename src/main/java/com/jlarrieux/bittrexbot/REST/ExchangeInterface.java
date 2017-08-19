package com.jlarrieux.bittrexbot.REST;



public interface ExchangeInterface {

    String getMarkets();
    String getMarketSummary(String marketName);
    String getMarketOrderBook(String marketName);
    String getMarketSummaries();
    String getOpenOrders();
    String getBalance(String currency);
    String getBalances();
    String cancelOrder(String id);
    String buy(String marketName, double quantity, double price);
    String sell(String marketName, double quantity, double price);



}
