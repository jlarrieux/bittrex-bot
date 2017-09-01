package com.jlarrieux.bittrexbot.simulation.TO;

public class MarketTO {
    private String marketCurrency = "";
    private String baseCurrency = "";
    private String isActive = "";
    private String minTradeSize = "";
    private String marketName = "";
    private String high = "";
    private String low = "";
    private String last = "";
    private String bid = "";
    private String ask = "";
    private String openBuyOrders = "";
    private String openSellOrders ="";
    private String volume = "";
    private String spread = "";

    public String getMarketCurrency() {
        return marketCurrency;
    }

    public void setMarketCurrency(String marketCurrency) {
        this.marketCurrency = marketCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getMinTradeSize() {
        return minTradeSize;
    }

    public void setMinTradeSize(String minTradeSize) {
        this.minTradeSize = minTradeSize;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getOpenBuyOrders() {
        return openBuyOrders;
    }

    public void setOpenBuyOrders(String openBuyOrders) {
        this.openBuyOrders = openBuyOrders;
    }

    public String getOpenSellOrders() {
        return openSellOrders;
    }

    public void setOpenSellOrders(String openSellOrders) {
        this.openSellOrders = openSellOrders;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }
}
