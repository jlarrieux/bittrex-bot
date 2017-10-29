package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.simulation.TO.*;
import com.jlarrieux.bittrexbot.simulation.db.DBConnectionPool;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static com.jlarrieux.bittrexbot.simulation.SimulationConstants.*;

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

    private final String START_HOUR_MINUTE ="00:00:01.0";
    private final String END_HOUR_MINUTE = "23:59:59.9" ;

    private DBConnectionPool dbConnectionPool;

    @Autowired
    public DBExchangeDAOImpl(DBConnectionPool dbConnectionPool, SimulationProperties simulationProperties) {

        log.info("Creating DBExchangeDAO...");

        this.dbConnectionPool = dbConnectionPool;

        dtfDate = DateTimeFormatter.ofPattern(simulationProperties.getDateFormatter());
        dtfTime = DateTimeFormatter.ofPattern(simulationProperties.getTimeFormatter());

        log.info("Retrieving DateStack...");

        long startTime = System.nanoTime();

        dateStack = getDateStack();

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
        PreparedStatement preparedStatment = null;
        ResultSet resultSet = null;

        try {
            connect =  getConnection();

            preparedStatment = connect.prepareStatement(GET_MARKET_SUMMARY);
            preparedStatment.setString(1, getDateInQuestion());
            preparedStatment.setString(2, marketName);

            resultSet = preparedStatment.executeQuery();

            /*log.debug("Inside: " + getClass().getSimpleName() +"\t Method: getMarketSummary()" +
                    "\t Date & Time currently in process: " + dateCurrentlyInProcess );*/

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
            close(resultSet, preparedStatment, connect);
        }

        return marketSummaryTO;
    }

    @Override
    public MarketSummariesTO getMarketSummaries() {
        MarketSummariesTO marketSummariesTO = getMarketSummariesForSpecificMarkets(null);
        return marketSummariesTO;
    }

    @Override
    public MarketSummariesTO getMarketSummaries(List<String> marketNames) {
        MarketSummariesTO marketSummariesTO = getMarketSummariesForSpecificMarkets(marketNames);
        return marketSummariesTO;
    }

    /**
     *
     * Method takes a list of markets and returns associated marketSummaries. If not markets
     * are provided, method will revert to default and retrieve marketSummaries for all markets
     *
     * @param markets
     * @return MarketSummariesTO
     */
    private MarketSummariesTO getMarketSummariesForSpecificMarkets(List<String> markets) {
        MarketSummariesTO marketSummariesTO = new MarketSummariesTO();
        Connection connect = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;

        try {
            connect = getConnection();
            markets = markets == null ? new ArrayList<>():markets;
            String queryString = createMarketSummariesQuery(markets.size());

            prepStatement = connect.prepareStatement(queryString);
            prepStatement.setString(1,getNextDateFromDateStack());

            if (markets != null && markets.size() > 0) {
                for(int i =1; i <= markets.size();i++) {
                    prepStatement.setString(i+1, markets.get(i-1));
                }
            }

            resultSet = prepStatement.executeQuery();

            //todo find a better way to get the list of markets
            String marketSize = markets.size() == 0 ? "" : Integer.toString(markets.size());
            log.debug("Inside: " + getClass().getSimpleName()
                    +"\t Method: getMarketSummaries(" + marketSize +")");
            log.debug("Date & Time currently in process: " + dateCurrentlyInProcess );

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
            close(resultSet, prepStatement, connect);
        }
        return marketSummariesTO;
    }
    @Override
    public MarketOrderBookTO getMarketOrderBook(String marketName) {

        MarketOrderBookTO  marketOrderBookTo = new MarketOrderBookTO();

        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connect = getConnection();

            preparedStatement = connect.prepareStatement(GET_ORDER_BOOK);
            preparedStatement.setString(1,getDateInQuestion());
            preparedStatement.setString(2, marketName);

            resultSet = preparedStatement.executeQuery();

            Double last = 0.0;

            if(resultSet.next()){
                last = resultSet.getDouble("last");
            }

            marketOrderBookTo.createNaddBuy(COIN_QUANTITY, last);
            marketOrderBookTo.createNaddSell(COIN_QUANTITY, last);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, preparedStatement, connect);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        BuyTO buyTO = null;
        try {
            connect = getConnection();

            insertOrder(uuid, marketName, quantity, price, "buy", preparedStatement, connect);

            buyTO = new BuyTO();
            BuyTO.Result result = buyTO.createResult();
            result.setUuid(uuid);
            buyTO.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, preparedStatement, connect);
        }

        return buyTO;
    }

    @Override
    public SellTO sell(String uuid, String marketName, double quantity, double price) {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        SellTO sellTO = null;
//        updateAvailalbeBalance(quantity, price); // todo remove comment

        try {
            connect = getConnection();
            insertOrder(uuid, marketName, quantity, price, "sell", preparedStatement, connect);

            sellTO = new SellTO();
            SellTO.Result result = sellTO.createResult();
            result.setUuid(uuid);
            sellTO.setResult(result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close(resultSet, preparedStatement, connect);
        }

        return sellTO;
    }

    @Override
    public OrderTO getOrder(String uuid) {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        OrderTO orderTO = new OrderTO();
        try {
            connect = getConnection();

            preparedStatement = connect.prepareStatement(GET_ORDER);
            preparedStatement.setString(1,uuid);

            resultSet = preparedStatement.executeQuery();

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
            close(resultSet, preparedStatement, connect);
        }
        return orderTO;
    }

    private void insertOrder(String uuid, String marketName, double quantity,
                             double price, String orderType, PreparedStatement preparedStmt,
                             Connection connect ) throws SQLException {

        long timeInMilis = System.currentTimeMillis();

        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMilis), ZoneId.systemDefault());
        String datePortionOfTime = dtfDate.format(localDateTime);
        String timePortionOfTime = dtfTime.format(localDateTime);

        java.sql.Timestamp timeInTimeStamp = new java.sql.Timestamp(timeInMilis);

        preparedStmt = connect.prepareStatement(INSERT_ORDER);
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

    private void close (ResultSet resultSet, PreparedStatement preparedStatement, Connection connect) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNextDateFromDateStack(){
        dateCurrentlyInProcess = (String) dateStack.pop();
        return dateCurrentlyInProcess ;
    }

    private String getDateInQuestion(){
        return dateCurrentlyInProcess ==""? (String) dateStack.peek(): dateCurrentlyInProcess;
    }


    private Stack getDateStack()  {
        Stack resultStack = new Stack();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();

            if (!simulationProperties.isUseBorderDates()) {
                preparedStatement = connection.prepareStatement(GET_DATE_STACK );
            } else {
                String startDate = simulationProperties.getStartDate() + " " + START_HOUR_MINUTE;
                String endDate = simulationProperties.getEndDate() + " " + END_HOUR_MINUTE;
                preparedStatement = connection.prepareStatement(GET_DATE_STACK_PER_DATES);
                preparedStatement.setString(1, startDate);
                preparedStatement.setString(2, endDate);
            }


            resultSet = preparedStatement.executeQuery();

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
            log.info("First Date to be processed: " + lastDate);
            log.info("Last Date to be processed: " + firstDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return resultStack;
    }

    private Connection getConnection(){
        Connection connect = null;
        try {
            connect = dbConnectionPool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }

    private static String createMarketSummariesQuery(int length) {
        String str = GET_MARKET_SUMMARIES;

        if (length > 0) {
            StringBuilder stringBuilder = new StringBuilder(GET_MARKET_SUMMARIES_MARKET_NAME_FOR_SPECIFIC_MARKETS);
            for(int i = 0; i < length; i++) {
                stringBuilder.append("?");
                if(i != length-1){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(")");
            str = stringBuilder.toString();
        }

        return str;
    }

    protected void populateInstances(DBConnectionPool dbConnectionPool, SimulationProperties simulationProperties) {
        this.dbConnectionPool = dbConnectionPool;
        this.simulationProperties = simulationProperties;
    }
   /* protected void updateAvailalbeBalance(double quantity, double price) {
        availableBalance += quantity*price;
    }*/ //todo remove comments
}