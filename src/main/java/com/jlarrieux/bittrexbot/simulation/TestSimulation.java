package com.jlarrieux.bittrexbot.simulation;


import com.jlarrieux.bittrexbot.Util.Analytics;
import lombok.Data;
import lombok.extern.java.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Log
@Data
public class TestSimulation {

    public static final String GET_MARKET_SUMMARIES_MARKET_NAME_FOR_SPECIFIC_MARKETS =
            "select  * from my_data inner join market on my_data.market_id = " +
                    "market.id where my_data.date_create = ? " +
                    "AND market.market_name in (";

    public static final String GET_MARKET_SUMMARIES = "select  * from my_data inner join market"
            + " on my_data.market_id=market.id where my_data.date_create= ? limit 0,199";

    private static String createQuery(int length) {
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

    public static void main(String[] args) {

      /*  String market = "BTC-1st,BTC-2st,BTC-3st,BTC-4st";
        StringTokenizer tokenizer = new StringTokenizer(market,",");
//        while(tokenizer.hasMoreElements())
        System.out.println("Token count: " + tokenizer.countTokens());
        //System.out.println("Token: " + tokenizer.nextToken());
        System.out.println("Dynamic Query: " + createQuery(tokenizer.countTokens()));*/

        /*StringBuilder strBuiler = new StringBuilder();
        strBuiler.append("*************************************************\n");
        Date date = new Date();
        strBuiler.append(date.toString());
        strBuiler.append("\t\t\t\t\t*");
        strBuiler.append("\n*************************************************\n");*/


/*double currentPortFolioValue = 175;
        double previousPortFolioValue = 175;
        double value = 0.0;
        value  = ((currentPortFolioValue - previousPortFolioValue)/previousPortFolioValue) * 100;
        System.out.println("Value: " + value);*/

        long timeInMilis = System.currentTimeMillis();
         DateTimeFormatter dtfDate = null;
         DateTimeFormatter dtfTime = null;
        dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMilis), ZoneId.systemDefault());
        String datePortionOfTime = dtfDate.format(localDateTime);
        String timePortionOfTime = dtfTime.format(localDateTime);
        System.out.println(localDateTime.toString());
        System.out.println(datePortionOfTime);
        System.out.println(timePortionOfTime);
        System.out.println(Analytics.OrderType.BUY.toString());


        String myDate = "2017-08-15 07:54:15.0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        System.out.println("Last Conversion: " + millis);
        localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        System.out.println(localDateTime.toString());


        /*log.info("Creating connection Pool...");
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl("jdbc:mysql://localhost:8888/bittrex");
            cpds.setUser("root");
            cpds.setPassword("dancehall1");
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(100);
            Connection connection= cpds.getConnection();
            DBExchangeDAOImpl dbExchangeDAO = new DBExchangeDAOImpl();
            dbExchangeDAO.printOutStack();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        //todo put good control to check connection is successful before after configuration
        //log.info("Connection pool Configured!");
       /*
        JSON Conversion
        MarketSummariesTO responseTO = new MarketSummariesTO();
        System.out.println(new Gson().toJson(responseTO));*/

       /* AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(SpringJDBCConfig.class);

        IDBExchangeDAO exchangeDAO = applicationContext.getBean(IDBExchangeDAO.class);

        System.out.println(exchangeDAO.getMarketSummaryFor1(1));

        applicationContext.close();*/

       /* To Test database connection

       DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
       MarketSummariesTO responseTO = dbExchangeDao.getMarketSummaries();
        System.out.println(new Gson().toJson(responseTO));
        responseTO = dbExchangeDao.getMarketSummaries();
        System.out.println(new Gson().toJson(responseTO.getResult()));*/


        /*DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
        //Example of Market BTC-1ST BTC-2GIVE BTC-ABY
        MarketOrderBookTO marketOrderBookTO = dbExchangeDao.getMarketOrderBook("BTC-1ST");
        System.out.println(new Gson().toJson(marketOrderBookTO));
        marketOrderBookTO = dbExchangeDao.getMarketOrderBook("BTC-2GIVE");
        System.out.println(new Gson().toJson(marketOrderBookTO));*/

        /*DBExchangeDAOImpl dbExchangeDAO = new DBExchangeDAOImpl();
        dbExchangeDAO.buy(UUID.randomUUID().toString(), "BTC-2GIVE",
        0.0404001, 0.3423432);*/

       /* java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        System.out.println("Current Time from Date: " + currentTime);*/

        /*DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("This is date: " + dtfDate.format(localDateTime));
        System.out.println("This is hour: " + dtfTime.format(localDateTime));*/


        /*DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
        //Example of Market BTC-1ST BTC-2GIVE BTC-ABY
        MarketSummaryTO marketSummaryTO = dbExchangeDao.getMarketSummary("BTC-1ST");
        System.out.println(new Gson().toJson(marketSummaryTO));
        marketSummaryTO = dbExchangeDao.getMarketSummary("BTC-2GIVE");
        System.out.println(new Gson().toJson(marketSummaryTO));*/

        /*Buy buy = new Buy();
        buy.setQuantity(1.0);
        buy.setRate(0.1);

        Sell sell = new Sell();
        sell.setQuantity(2.0);
        sell.setRate(0.2);

        Buy buy2 = new Buy();
        buy2.setQuantity(3.0);
        buy2.setRate(0.3);

        Sell sell2 = new Sell();
        sell2.setQuantity(4.0);
        sell2.setRate(0.4);

        mbTO.getResult().getBuy().add(buy);
        mbTO.getResult().getBuy().add(buy2);

        mbTO.getResult().getSell().add(sell);
        mbTO.getResult().getSell().add(sell2);

        System.out.println(new Gson().toJson(mbTO));*/



        //JSON Conversion
       /* MarketSummariesTO responseTO = new MarketSummariesTO();

        Market market = new Market();
        market.setMarketCurrency("1ST");
        market.setMinTradeSize(0.00000001);

        Summary summary = new Summary();
        summary.setMarketName("BTC-1ST");
        summary.setHigh(0.00012801);

        Result result = new Result();
        result.setIsVerified(true);
        result.setMarket(market);
        result.setSummary(summary);

        responseTO.getResult().add(result);
        System.out.println(new Gson().toJson(responseTO));*/

        /*DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        dbExchangeDAO.printOutStack();*/

    }
}
