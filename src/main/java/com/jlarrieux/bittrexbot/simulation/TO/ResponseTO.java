package com.jlarrieux.bittrexbot.simulation.TO;

import java.util.ArrayList;
import java.util.List;

public class ResponseTO {
    private String success="true";
    private String message="";
    private List result = new ArrayList();

    public ResponseTO(){
        MarketSet marketWhole = new MarketSet();
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

    public MarketSet createMarketSet(){
        return new MarketSet();
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
    public class MarketSet {
        Market Market = new Market();
        Summary Summary = new Summary();
        boolean IsVerified = false;

        public ResponseTO.Market getMarket() {
            return Market;
        }

        public Summary getSummary(){
            return Summary;
        }

        public void setmarketCurrenty(String marketCurrency) {
            Market.MarketCurrency = marketCurrency;
        }

        public void setIsActive(boolean bool) {
            Market.IsActive = bool;
        }

        public void setMinTradeSize(double minTradeSize) {
            Market.MinTradeSize = minTradeSize;
        }

        public void setMarketName(String marketName) {
            Market.MarketName = marketName;
        }



    }

}
