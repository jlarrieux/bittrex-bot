package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.simulation.TO.*;

public interface IDBExchangeDAO {
    public MarketSummariesTO getMarketSummaries();
    public MarketOrderBookTO getMarketOrderBook(String marketName);
    public MarketSummaryTO getMarketSummary(String marketName);
    public BuyTO buy(String uuid, String marketName, double quantity, double price);
    public SellTO sell(String uuid, String marketName, double quantity, double price);
    public OrderTO getOrder(String uuid);

}
