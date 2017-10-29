package com.jlarrieux.bittrexbot.simulation;

public class SimulationConstants {

    public static final String GET_MARKET_SUMMARIES_MARKET_NAME =
            "select  * from my_data inner join market on my_data.market_id = " +
                    "market.id where my_data.date_create = ?" +
                    "AND market.market_name = ? limit 0,1";

    //This statment is to be completed by client class
    public static final String GET_MARKET_SUMMARIES_MARKET_NAME_FOR_SPECIFIC_MARKETS =
            "select  * from my_data inner join market on my_data.market_id = " +
                    "market.id where my_data.date_create = ? " +
                    "AND market.market_name in (";

    public static final String GET_MARKET_SUMMARIES = "select  * from my_data inner join market"
            + " on my_data.market_id=market.id where my_data.date_create= ? limit 0,199";

    public static final String GET_ORDER_BOOK = "select my_data.last from my_data inner join "
            + "market on my_data.market_id=market.id where my_data.date_create = ? " +
            "AND market.market_name= ? limit 0, 1";

    public static final String GET_ORDER = "select * from orders_sim where uuid = ?";

    public static final String GET_MARKET_SUMMARY = "select * from my_data inner join "
            + "market on my_data.market_id=market.id where my_data.date_create= ? "
            + "AND market.market_name= ? limit 0, 1";

    public static final String GET_DATE_STACK = "select distinct date_create from " +
            "my_data order by date_create DESC";

    public static final String GET_DATE_STACK_PER_DATES = "select distinct date_create from my_data where " +
            "date_create >= ? AND date_create <= ? order by date_create DESC";

    public static final String INSERT_ORDER = "Insert into orders_sim values (?,?,?,?,?,?,?,?)";
}
