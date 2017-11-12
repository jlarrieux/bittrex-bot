package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.*;

import java.util.List;

public interface IDBExchangeDAO {
    public MarketSummariesTO getMarketSummaries();
    public MarketSummariesTO getMarketSummaries(List<String> marketNames);
    public MarketOrderBookTO getMarketOrderBook(String marketName);
    public String getDateCurrentlyInProcess();
    public MarketSummaryTO getMarketSummary(String marketName);
    public BuyTO buy(String uuid, String marketName, double quantity, double price);
    public SellTO sell(String uuid, String marketName, double quantity, double price);
    public OrderTO getOrder(String uuid);
    public BalanceTO getBalance(String currency);

}
