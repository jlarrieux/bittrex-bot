package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;
import com.jlarrieux.bittrexbot.simulation.TO.Market;
import com.jlarrieux.bittrexbot.simulation.TO.Result;
import com.jlarrieux.bittrexbot.simulation.TO.Summary;
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

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTTION_URL = "jdbc:mysql://localhost/bittrex?user=root&password=root";


    public DBExchangeDAOImpl() {
        boolean startFromMostRecentDate = false;
        dateStack = getDateStack(startFromMostRecentDate);
        System.out.println("Start from most recent date: " + startFromMostRecentDate);
    }

   /* @Override
    public String getMarketSummaryFor1(int id){
        String sql = "select market_currency from market where id = ?";
        String name = jdbcTemplate.queryForObject(sql,new Object[]{id},String.class);
        return name;
    }*/





    public void printOutStack(){
        while (!dateStack.isEmpty()) {
            System.out.println(dateStack.pop());
        }
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
                            + dateStack.pop() +"'");

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

