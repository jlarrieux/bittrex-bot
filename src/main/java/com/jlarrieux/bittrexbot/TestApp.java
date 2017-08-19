package com.jlarrieux.bittrexbot;



import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;


//@Log
//@SpringBootApplication
//@ConditionalOnMissingBean(BittrexBotApplication.class)
public class TestApp {

    public static void main(String[] args){
        ConfigurableApplicationContext ctx = SpringApplication.run(TestApp.class, args);
//        Trader trader = ctx.getBean(Trader.class);
//        trader.cancelOldOrders();
//        MyBittrexClient client = ctx.getBean(MyBittrexClient.class);
//        log.info( client.getMarketOrderBook("BTC-LTC"));
//        PortFolio portFolio = ctx.getBean(PortFolio.class);
//        log.info(String.format("Portfolio value: %f", portFolio.getCurrentPortFolioValue()));

//        PositionManager manager = ctx.getBean(PositionManager.class);
//        MyBittrexClient client = ctx.getBean(MyBittrexClient.class);
//        log.info( client.getBalances());
//        manager.cleanSlate();
//        client.getBalances();
    }





}
