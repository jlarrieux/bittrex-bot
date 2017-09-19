package com.jlarrieux.bittrexbot.simulation;

import com.google.gson.Gson;
import com.jlarrieux.bittrexbot.simulation.DAO.DBExchangeDAOImpl;
import com.jlarrieux.bittrexbot.simulation.TO.Buy;
import com.jlarrieux.bittrexbot.simulation.TO.MarketOrderBookTO;
import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;
import com.jlarrieux.bittrexbot.simulation.TO.Sell;

public class TestSimulation {

    public static void main(String[] args) {
       /*
        JSON Conversion
        ResponseTO responseTO = new ResponseTO();
        System.out.println(new Gson().toJson(responseTO));*/

       /* AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(SpringJDBCConfig.class);

        IDBExchangeDAO exchangeDAO = applicationContext.getBean(IDBExchangeDAO.class);

        System.out.println(exchangeDAO.getMarketSummaryFor1(1));

        applicationContext.close();*/

       /* To Test database connection

       DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
       ResponseTO responseTO = dbExchangeDao.getMarketSummaries();
        System.out.println(new Gson().toJson(responseTO));
        responseTO = dbExchangeDao.getMarketSummaries();
        System.out.println(new Gson().toJson(responseTO.getResult()));*/


        DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
        //Example of Market BTC-1ST BTC-2GIVE BTC-ABY
        MarketOrderBookTO marketOrderBookTO = dbExchangeDao.getMarketOrderBook("BTC-1ST");
        System.out.println(new Gson().toJson(marketOrderBookTO));
        marketOrderBookTO = dbExchangeDao.getMarketOrderBook("BTC-2GIVE");
        System.out.println(new Gson().toJson(marketOrderBookTO.getResult()));

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
       /* ResponseTO responseTO = new ResponseTO();

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



    }
}
