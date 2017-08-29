package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;



@Component
@Data
@Log
@Qualifier(Constants.SIMULATOR)
@Profile("!live")
public class SimulatorExchange  implements ExchangeInterface{


    @Override
    public Response getMarkets() {
        return null;
    }



    @Override
    public Response getMarketSummary(String marketName) {
        return null;
    }



    @Override
    public Response getMarketOrderBook(String marketName) {
        return null;
    }



    @Override
    public Response getMarketSummaries() {
        return null;
    }



    @Override
    public Response getOpenOrders() {
        return null;
    }



    @Override
    public Response getBalance(String currency) {
        return null;
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
        return null;
    }



    @Override
    public Response sell(String marketName, double quantity, double price) {
        return null;
    }



    @Override
    public Response getOrderHistory(String marketName) {
        return null;
    }



    @Override
    public Response getOrder(String uuid) {
        return null;
    }
}
