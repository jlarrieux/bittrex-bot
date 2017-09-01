package com.jlarrieux.bittrexbot.simulation;

import com.jlarrieux.bittrexbot.simulation.DAO.DBExchangeDAOImpl;
import com.jlarrieux.bittrexbot.simulation.DAO.DBExchangeDAOImpl2;
import com.jlarrieux.bittrexbot.simulation.DAO.IDBExchangeDAO;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

       IDBExchangeDAO dbExchangeDao = new DBExchangeDAOImpl2();
        dbExchangeDao.getMarketSummaryFor1(1);


    }
}
