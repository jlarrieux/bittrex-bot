package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.*;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.*;
import java.util.Stack;

public class DBExchangeDAOImpl implements IDBExchangeDAO {
    private JdbcTemplate jdbcTemplate;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private static Stack dateStack = new Stack();
    private static String dateInQuestion = "";
    private static final Double COIN_QUANTITY  = 1000000.0;

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTTION_URL = "jdbc:mysql://localhost/bittrex?user=root&password=root";


    public DBExchangeDAOImpl() {
        boolean startFromMostRecentDate = false;
        dateStack = getDateStack(startFromMostRecentDate);
        System.out.println("Start from most recent date: " + startFromMostRecentDate);
    }

    public void printOutStack(){
        while (!dateStack.isEmpty()) {
            System.out.println(getNextDateFromDateStack());
        }
    }

    @Override
    public MarketSummaryTO getMarketSummary(String marketName) {
        MarketSummaryTO marketSummaryTO = new MarketSummaryTO();

        try {
            Class.forName(JDBC_DRIVER);
            connect = DriverManager.getConnection(DB_CONNECTTION_URL);
            statement = connect.createStatement();
            String str = "select * from my_data inner join "
                    + "market on my_data.market_id=market.id where my_data.date_create='"
                    + getDateInQuestion() + "' AND market.market_name='" + marketName +"' limit 0, 1";
            resultSet = statement.executeQuery(str);

            while (resultSet.next()) {

                MarketSummaryTO.Result result = marketSummaryTO.createResult();
                marketSummaryTO.setMessage("");
                marketSummaryTO.setSuccess(true);
                marketSummaryTO.getResult().add(result);

                result.setMarketName(resultSet.getString("market_name"));
                result.setAsk(resultSet.getDouble("ask"));
                result.setBid(resultSet.getDouble("bid"));
                result.setHigh((resultSet.getDouble("high")));
                result.setLast(resultSet.getDouble("last"));
                result.setLow(resultSet.getDouble("low"));
                result.setOpenBuyOrders(resultSet.getInt("open_buy_orders"));
                result.setOpenSellOrders(resultSet.getInt("open_sell_orders"));
                result.setVolume(resultSet.getDouble("volume"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return marketSummaryTO;
    }

    public void setJdbcTemplate (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseTO getMarketSummaries() {
        ResponseTO responseTO = new ResponseTO();
        try {

            Class.forName(JDBC_DRIVER);
            connect = DriverManager.getConnection(DB_CONNECTTION_URL);
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select  * from my_data inner join market"
                            + " on my_data.market_id=market.id where my_data.date_create='"
                            + getNextDateFromDateStack() +"' limit 0,199");

            while (resultSet.next()) {
                Summary summary = new Summary();
                summary.setAsk(resultSet.getDouble("ask"));
                summary.setBid(resultSet.getDouble("bid"));
                summary.setHigh((resultSet.getDouble("high")));
                summary.setLast(resultSet.getDouble("last"));
                summary.setLow(resultSet.getDouble("low"));
                summary.setOpenBuyOrders(resultSet.getInt("open_buy_orders"));
                summary.setOpenSellOrders(resultSet.getInt("open_sell_orders"));
                summary.setVolume(resultSet.getDouble("volume"));
                summary.setMarketName(resultSet.getString("market_name"));

                Market market = new Market();
                market.setBaseCurrency(resultSet.getString("base_currency"));
                market.setMarketName(resultSet.getString("market_name"));
                market.setMinTradeSize(resultSet.getDouble("min_trade_size"));
                market.setIsActive(resultSet.getBoolean("is_active"));
                market.setMarketCurrency(resultSet.getString("market_currency"));
                market.setCreated(resultSet.getString("date_create"));

                Result result = new Result();
                result.setSummary(summary);
                result.setMarket(market);
                result.setIsVerified(false);

                responseTO.getResult().add(result);
            }
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            close();
        }
        return responseTO;
    }

    @Override
    public MarketOrderBookTO getMarketOrderBook(String marketName) {
        MarketOrderBookTO  marketOrderBookTo = new MarketOrderBookTO();

        try {
            Class.forName(JDBC_DRIVER);
            connect = DriverManager.getConnection(DB_CONNECTTION_URL);
            statement = connect.createStatement();
            String str = "select my_data.last from my_data inner join "
                    + "market on my_data.market_id=market.id where my_data.date_create='"
                    + getDateInQuestion() + "' AND market.market_name='" + marketName +"' limit 0, 1";
            resultSet = statement.executeQuery(str);

            Double last = 0.0;
            if(resultSet.next()){
                last = resultSet.getDouble("last");
            }

            marketOrderBookTo.createNaddBuy(COIN_QUANTITY, last);
            marketOrderBookTo.createNaddSell(COIN_QUANTITY, last);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close();
        }
        return marketOrderBookTo;
    }

    private void fecthMarketInfo(Integer marketId) {

    }

    private void close () {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    private String getNextDateFromDateStack(){
        dateInQuestion = (String) dateStack.peek();
        return (String) dateStack.pop();
    }

    private String getDateInQuestion(){
        return dateInQuestion ==""? (String) dateStack.peek(): dateInQuestion;
    }


    private Stack getDateStack(boolean startFromMostRecentDate)  {
        Stack resultStack = new Stack();

        try {

            String dateOrder = "desc";

            Class.forName(JDBC_DRIVER);

            connect = DriverManager.getConnection(DB_CONNECTTION_URL);

            statement = connect.createStatement();

            if (startFromMostRecentDate) {
                dateOrder = "asc";
            }

            // select dates in reverse order
            resultSet = statement.executeQuery("select distinct date_create from my_data order by date_create " + dateOrder);

            while (resultSet.next()) {
                resultStack.push(resultSet.getString("date_create"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return resultStack;
    }
}

