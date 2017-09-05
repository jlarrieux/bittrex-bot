package com.jlarrieux.bittrexbot.simulation;

import com.google.gson.Gson;
import com.jlarrieux.bittrexbot.simulation.DAO.DBExchangeDAOImpl;
import com.jlarrieux.bittrexbot.simulation.TO.ResponseTO;

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

        DBExchangeDAOImpl dbExchangeDao = new DBExchangeDAOImpl();
        //dbExchangeDao.printOutStack();
       ResponseTO responseTO = dbExchangeDao.getMarketSummaryFor1(1);
        System.out.println(new Gson().toJson(responseTO));
        responseTO = dbExchangeDao.getMarketSummaryFor1(1);
        System.out.println(new Gson().toJson(responseTO.getResult()));

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
