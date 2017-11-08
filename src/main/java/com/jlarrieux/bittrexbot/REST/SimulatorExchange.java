package com.jlarrieux.bittrexbot.REST;


import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.simulation.DAO.IDBExchangeDAO;
import com.jlarrieux.bittrexbot.simulation.TO.BalanceTO;
import com.jlarrieux.bittrexbot.simulation.TO.BuyTO;
import com.jlarrieux.bittrexbot.simulation.TO.OrderTO;
import com.jlarrieux.bittrexbot.simulation.TO.SellTO;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;


@Component
@Data
@Log
@Qualifier(Constants.SIMULATOR)
@Profile("!live")
public class SimulatorExchange  implements ExchangeInterface{

    @Autowired
    @Qualifier("inMemoryDbDAO")
    private IDBExchangeDAO dbExchangeDAO;

    @Autowired
    private SimulationProperties simulationProperties;


    private double availableBalance;

    @Autowired
    public SimulatorExchange(SimulationProperties simulationProperties){
        availableBalance = simulationProperties.getSimlatorExchangeUserAvaialbleBalance();
    }

    @Override
    public Response getMarkets() {
        return new MyBittrexClient().getMarkets();
    }



    @Override
    public Response getMarketSummary(String marketName) {
        return new Response(dbExchangeDAO.getMarketSummary(marketName));
    }



    @Override
    public Response getMarketOrderBook(String marketName) {
        return new Response(dbExchangeDAO.getMarketOrderBook(marketName));
    }



    @Override
    public Response getMarketSummaries() {
        return new Response(dbExchangeDAO.getMarketSummaries());
    }

    @Override
    public Response getMarketSummaries(String marketNames) {
        Response response = null;
        if (marketNames == "" || marketNames == null) {
            response = getMarketSummaries();

        } else{
            StringTokenizer tokenizer = new StringTokenizer(marketNames,",");
            ArrayList<String> marketList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                marketList.add(tokenizer.nextToken());
            }
            response = new Response(dbExchangeDAO.getMarketSummaries(marketList));
        }
        return response;
    }


    @Override
    public Response getOpenOrders() {
        return null;
    }



    @Override
    public Response getBalance(String currency) {
        BalanceTO balanceTO = new BalanceTO();
        BalanceTO.Result result = balanceTO.createResult();
        result.setAvailable(availableBalance);
        balanceTO.setResult(result);
        return new Response(balanceTO);
    }



    @Override
    public Response getBalances() {
        return null;
    }



    @Override
    public Response cancelOrder(String id) {
        return null;
    }



    @Override
    public Response buy(String marketName, double quantity, double price) {
        double value = quantity*price;
        if(value>availableBalance){
            Response r = new Response(new BuyTO());
            r.setSuccess(false);
            return r;
        }
        else {
            BuyTO buyTO = dbExchangeDAO.buy(generateUUID(), marketName, quantity, price);
            availableBalance -= value;
            return new Response(buyTO);
        }
    }



    @Override
    public Response sell(String marketName, double quantity, double price) {
        SellTO  sellTO= dbExchangeDAO.sell(generateUUID(),marketName, quantity, price);
        if(sellTO!=null) {
            availableBalance += quantity*price;
            return new Response(sellTO);
        }
        return null;
    }

    @Override
    public Response getOrderHistory(String marketName) {
        return null;
    }

    @Override
    public Response getOrder(String uuid) {
        OrderTO orderTO = dbExchangeDAO.getOrder(uuid);
        return new Response(orderTO);
    }

    private String generateUUID(){//TODO use enum on orderType
       return  UUID.randomUUID().toString();
    }

    public class Result {

        private String uuid;

        public String getUuid() {
            return uuid;
        }

        public Result(String uuid) {
            this.uuid = uuid;
        }
        }
}
