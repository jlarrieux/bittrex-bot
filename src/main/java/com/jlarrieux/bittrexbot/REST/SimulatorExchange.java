package com.jlarrieux.bittrexbot.REST;



import com.jlarrieux.bittrexbot.Util.Constants;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;



@Data
@Log
@Qualifier(Constants.SIMULATOR)
public class SimulatorExchange  implements ExchangeInterface{


    @Override
    public String getMarkets() {
        return null;
    }



    @Override
    public String getMarketSummary(String marketName) {
        return null;
    }



    @Override
    public String getMarketOrderBook(String marketName) {
        return null;
    }



    @Override
    public String getMarketSummaries() {
        return null;
    }



    @Override
    public String getOpenOrders() {
        return null;
    }



    @Override
    public String getBalance(String currency) {
        return null;
    }



    @Override
    public String getBalances() {
        return null;
    }



    @Override
    public String cancelOrder(String id) {
        return null;
    }



    @Override
    public String buy(String marketName, double quantity, double price) {
        return null;
    }



    @Override
    public String sell(String marketName, double quantity, double price) {
        return null;
    }
}
