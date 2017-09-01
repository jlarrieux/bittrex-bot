package com.jlarrieux.bittrexbot.simulation;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ResponseTO {
    private String success="true";
    private String message="";
    private List result = new ArrayList();

    public ResponseTO(){
        MarketWhole marketWhole = new MarketWhole();
        result.add(marketWhole);
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public MarketWhole createMarketWhole(){
        return new MarketWhole();
    }

    public class Market {
        String MarketCurrency="";
        String BaseCurrency = "";
        boolean IsActive;
        double MinTradeSize;
        String MarketName = "";
    }

    public class Summary{
        String MarketName = "";
        double High, Low, Last, Bid, Ask,Volume;
        int OpenBuyOrders;
        int OpenSellOrders;


    }
    public class MarketWhole{
        Market Market = new Market();
        Summary Summary = new Summary();
        boolean IsVerified = false;

        public ResponseTO.Market getMarket() {
            return Market;
        }

        public Summary getSummary(){
            return Summary;
        }
    }

}
