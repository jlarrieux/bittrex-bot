package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.MarketOrderBookTO;
import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;

public interface IDBExchangeDAO {
    public ResponseTO getMarketSummaries();
    public MarketOrderBookTO getMarketOrderBook(String marketName);

}
