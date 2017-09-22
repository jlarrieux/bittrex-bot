package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.simulation.TO.MarketOrderBookTO;
import com.jlarrieux.bittrexbot.simulation.TO.MarketSummaryTO;
import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;

public interface IDBExchangeDAO {
    public ResponseTO getMarketSummaries();
    public MarketOrderBookTO getMarketOrderBook(String marketName);
    public MarketSummaryTO getMarketSummary(String marketName);
    public Order buy(String uuid, String marketName, double quantity, double price);
    public Order sell(String uuid, String marketName, double quantity, double price);

}
