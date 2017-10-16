package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.simulation.TO.*;
import com.jlarrieux.bittrexbot.simulation.db.DBConnectionPool;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class DBExchangeDAOImpl implements IDBExchangeDAO {


    public double availableBalance = 1000.0; //TODO needs to be taken from property file
    private static Stack dateStack = new Stack();
    private static String dateCurrentlyInProcess = "";

    private static SimulationProperties simulationProperties;

    private static final Double COIN_QUANTITY = 1000000.0; // TODO: 10/8/2017 Needs to be taken from proprty file

    private DateTimeFormatter dtfDate;
    private DateTimeFormatter dtfTime;

    private DBConnectionPool dbConnectionPool;

    //todo refactor create connection
    //todo Used prepared statement

    @Autowired
    public DBExchangeDAOImpl(DBConnectionPool dbConnectionPool, SimulationProperties simulationProperties) {

        log.info("Creating DBExchangeDAO...");
        this.simulationProperties = simulationProperties;
        boolean startFromMostRecentDate = simulationProperties.isStartFromRecentDate();
        this.dbConnectionPool = dbConnectionPool;
        dtfDate = DateTimeFormatter.ofPattern(simulationProperties.getDateFormatter());
        dtfTime = DateTimeFormatter.ofPattern(simulationProperties.getTimeFormatter());

        log.info("Retrieving DateStack...");
        long startTime = System.nanoTime();
        dateStack = getDateStack(startFromMostRecentDate);
        long totalTime = System.nanoTime() - startTime;
        log.info("Retrieving DateStack complete. Time elapsed: " +
                String.format("%d min, %d sec", TimeUnit.NANOSECONDS.toHours(totalTime),
                TimeUnit.NANOSECONDS.toSeconds(totalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(totalTime))));
        log.info("DBExchangeDAO creation successful!");
    }

    public DBExchangeDAOImpl() {

    }
    public void printOutStack(){
        while (!dateStack.isEmpty()) {
            System.out.println(getNextDateFromDateStack());
        }
    }

    @Override
    public MarketSummaryTO getMarketSummary(String marketName) {
        MarketSummaryTO marketSummaryTO = new MarketSummaryTO();
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connect =  getConnection();
            statement = connect.createStatement();
            String str = "select * from my_data inner join "
                    + "market on my_data.market_id=market.id where my_data.date_create='"
                    + getDateInQuestion() + "' AND market.market_name='" + marketName +"' limit 0, 1";
            resultSet = statement.executeQuery(str);
            log.info("Inside: " + getClass().getSimpleName() +"\t Method: getMarketSummary()" +
                    "\t Date & Time currently in process" +
                    dateCurrentlyInProcess );
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
            close(resultSet, statement, connect);
        }

        return marketSummaryTO;
    }

    @Override
    public MarketSummariesTO getMarketSummaries() {
        MarketSummariesTO marketSummariesTO = new MarketSummariesTO();
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connect = getConnection();
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select  * from my_data inner join market"
                            + " on my_data.market_id=market.id where my_data.date_create='"
                            + getNextDateFromDateStack() +"' limit 0,199");
            log.info("Inside: " + getClass().getSimpleName() +"\t Method: getMarketSummaries()" +
                    "\t Date & Time currently in process" +
                    dateCurrentlyInProcess );

            while (resultSet.next()) {
                MarketSummariesTO.Summary  summary = new MarketSummariesTO().createSummary();
                summary.setAsk(resultSet.getDouble("ask"));
                summary.setBid(resultSet.getDouble("bid"));
                summary.setHigh((resultSet.getDouble("high")));
                summary.setLast(resultSet.getDouble("last"));
                summary.setLow(resultSet.getDouble("low"));
                summary.setOpenBuyOrders(resultSet.getInt("open_buy_orders"));
                summary.setOpenSellOrders(resultSet.getInt("open_sell_orders"));
                summary.setVolume(resultSet.getDouble("volume"));
                summary.setMarketName(resultSet.getString("market_name"));

                MarketSummariesTO.Market market = new MarketSummariesTO().createMarket();
                market.setBaseCurrency(resultSet.getString("base_currency"));
                market.setMarketName(resultSet.getString("market_name"));
                market.setMinTradeSize(resultSet.getDouble("min_trade_size"));
                market.setIsActive(resultSet.getBoolean("is_active"));
                market.setMarketCurrency(resultSet.getString("market_currency"));
                market.setCreated(resultSet.getString("date_create"));

                MarketSummariesTO.Result result = new MarketSummariesTO().createResult();
                result.setSummary(summary);
                result.setMarket(market);
                result.setIsVerified(false);

                marketSummariesTO.getResult().add(result);
            }
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            close(resultSet, statement, connect);
        }
        return marketSummariesTO;
    }

    @Override
    public MarketSummariesTO getMarketSummaries(String marketName) {
        MarketSummariesTO marketSummariesTO = new MarketSummariesTO();
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connect = getConnection();

            statement = connect.createStatement();

            resultSet = statement.executeQuery("select  * from my_data inner join market"
                    + " on my_data.market_id=market.id where my_data.date_create='"
                    + getNextDateFromDateStack() +"' AND market.market_name='"
                    + marketName + "'limit 0,1");

            log.info("Inside: " + getClass().getSimpleName() +"\t Method: getMarketSummaries()" +
                    "\t Date & Time currently in process" +
                    dateCurrentlyInProcess );

            while (resultSet.next()) {
                MarketSummariesTO.Summary  summary = new MarketSummariesTO().createSummary();
                summary.setAsk(resultSet.getDouble("ask"));
                summary.setBid(resultSet.getDouble("bid"));
                summary.setHigh((resultSet.getDouble("high")));
                summary.setLast(resultSet.getDouble("last"));
                summary.setLow(resultSet.getDouble("low"));
                summary.setOpenBuyOrders(resultSet.getInt("open_buy_orders"));
                summary.setOpenSellOrders(resultSet.getInt("open_sell_orders"));
                summary.setVolume(resultSet.getDouble("volume"));
                summary.setMarketName(resultSet.getString("market_name"));

                MarketSummariesTO.Market market = new MarketSummariesTO().createMarket();
                market.setBaseCurrency(resultSet.getString("base_currency"));
                market.setMarketName(resultSet.getString("market_name"));
                market.setMinTradeSize(resultSet.getDouble("min_trade_size"));
                market.setIsActive(resultSet.getBoolean("is_active"));
                market.setMarketCurrency(resultSet.getString("market_currency"));
                market.setCreated(resultSet.getString("date_create"));

                MarketSummariesTO.Result result = new MarketSummariesTO().createResult();
                result.setSummary(summary);
                result.setMarket(market);
                result.setIsVerified(false);

                marketSummariesTO.getResult().add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, statement, connect);
        }
        return marketSummariesTO;
    }
    @Override
    public MarketOrderBookTO getMarketOrderBook(String marketName) {
        MarketOrderBookTO  marketOrderBookTo = new MarketOrderBookTO();

        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connect = getConnection();
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
            close(resultSet, statement, connect);
        }
        return marketOrderBookTo;
    }

    //todo: clean this mess up see simulator line 45
    @Override
    public BalanceTO getBalance(String currency) {
        BalanceTO balanceTO = new BalanceTO();
        BalanceTO.Result result = balanceTO.createResult();
        result.setAvailable(availableBalance);
        balanceTO.setResult(result);
        return balanceTO;
    }

    @Override
    public BuyTO buy(String uuid, String marketName, double quantity, double price) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BuyTO buyTO = null;
        try {
            connect = getConnection();
            statement = connect.createStatement();

            insertOrder(uuid, marketName, quantity, price, "buy", statement, connect);

            buyTO = new BuyTO();
            BuyTO.Result result = buyTO.createResult();
            result.setUuid(uuid);
            buyTO.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, statement, connect);
        }

        return buyTO;
    }

    @Override
    public SellTO sell(String uuid, String marketName, double quantity, double price) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SellTO sellTO = null;
        updateAvailalbeBalance(quantity, price);

        try {
            connect = getConnection();
            statement = connect.createStatement();

            insertOrder(uuid, marketName, quantity, price, "sell", statement, connect);

            sellTO = new SellTO();
            SellTO.Result result = sellTO.createResult();
            result.setUuid(uuid);
            sellTO.setResult(result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, statement, connect);
        }

        return sellTO;
    }

    @Override
    public OrderTO getOrder(String uuid) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        OrderTO orderTO = new OrderTO();
        try {
            connect = getConnection();
            statement = connect.createStatement();

            String str = "select * from orders_sim where uuid = '" + uuid + "'";
            resultSet = statement.executeQuery(str);

            orderTO = new OrderTO();
            OrderTO.Result result = orderTO.createResult();
            orderTO.setResult(result);

            while (resultSet.next()) {
                result.setOrderUuid(resultSet.getString("uuid"));
                result.setQuantity(resultSet.getDouble("quantity"));
                result.setPrice(resultSet.getDouble("price"));
                result.setExchange(resultSet.getString("market_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, statement, connect);
        }
        return orderTO;
    }

    private void insertOrder(String uuid, String marketName, double quantity,
                             double price, String orderType, Statement statement,
                             Connection connect ) throws SQLException {
        String sql = "Insert into orders_sim values (?,?,?,?,?,?,?,?)";

        long timeInMilis = System.currentTimeMillis();

        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMilis), ZoneId.systemDefault());
        String datePortionOfTime = dtfDate.format(localDateTime);
        String timePortionOfTime = dtfTime.format(localDateTime);

        java.sql.Timestamp timeInTimeStamp = new java.sql.Timestamp(timeInMilis);

        PreparedStatement preparedStmt = connect.prepareStatement(sql);
        preparedStmt.setString(1, uuid);
        preparedStmt.setString(2, marketName);
        preparedStmt.setDouble(3, quantity);
        preparedStmt.setDouble(4, price);
        preparedStmt.setTimestamp(5,timeInTimeStamp);
        preparedStmt.setString(6,datePortionOfTime);
        preparedStmt.setString(7,timePortionOfTime);
        preparedStmt.setString(8, orderType); //todo use enum in code, constraints in db

        preparedStmt.execute();

    }

    private void fecthMarketInfo(Integer marketId) {

    }

    private void close (ResultSet resultSet, Statement statement, Connection connect) {
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
            e.printStackTrace();
        }
    }

    private String getNextDateFromDateStack(){
        dateCurrentlyInProcess = (String) dateStack.peek();
        return (String) dateStack.pop();
    }

    private String getDateInQuestion(){
        return dateCurrentlyInProcess ==""? (String) dateStack.peek(): dateCurrentlyInProcess;
    }


    private Stack getDateStack(boolean startFromMostRecentDate)  {
        Stack resultStack = new Stack();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            String dateOrder = "desc";

            connection = getConnection();

            statement = connection.createStatement();

            if (startFromMostRecentDate) {
                dateOrder = "asc";
            }

            // select dates in reverse order
            resultSet = statement.executeQuery("select distinct date_create from " +
                    "my_data order by date_create " + dateOrder);

            int dateCount = 0;
            String firstDate = "";
            String lastDate = "";
            while (resultSet.next()) {
                dateCount++;
                String tempStr = resultSet.getString("date_create");

                if(dateCount == 1){firstDate = tempStr;}

                resultStack.push(tempStr);

                lastDate = tempStr;
            }
            log.info("Number of dates in stack: " + dateCount);
            log.info("First date in Stack: " + firstDate);
            log.info("Last Date in Stack: " + lastDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, statement, connection);
        }
        return resultStack;
    }

    private Connection getConnection(){
        // TODO: 10/8/2017 Remember to remove comment block
        /* if (dbConnectionPool == null) {
            try {
                dbConnectionPool = DBConnectionPool.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        Connection connect = null;
        try {
            connect = dbConnectionPool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }

    protected void populateInstances(DBConnectionPool dbConnectionPool, SimulationProperties simulationProperties) {
        this.dbConnectionPool = dbConnectionPool;
        this.simulationProperties = simulationProperties;
    }
    protected void updateAvailalbeBalance(double quantity, double price) {
        availableBalance += quantity*price;
    }


}

