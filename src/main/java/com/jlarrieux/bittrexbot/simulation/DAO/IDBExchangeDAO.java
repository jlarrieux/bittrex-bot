package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;

public interface IDBExchangeDAO {
    public ResponseTO getMarketSummaries();

    public String getMarketSummaryFor1(int d);
}
