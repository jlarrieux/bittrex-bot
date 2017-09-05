package com.jlarrieux.bittrexbot.simulation.TO;

public class Summary {

    private String MarketName;
    private Double High;
    private Double Low;
    private Double Volume;
    private Double Last;
    private Double BaseVolume;
    private String TimeStamp;
    private Double Bid;
    private Double Ask;
    private Integer OpenBuyOrders;
    private Integer OpenSellOrders;
    private Double PrevDay;
    private String Created;

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        this.MarketName = marketName;
    }

    public Double getHigh() {
        return High;
    }

    public void setHigh(Double high) {
        this.High = high;
    }

    public Double getLow() {
        return Low;
    }

    public void setLow(Double low) {
        this.Low = low;
    }

    public Double getVolume() {
        return Volume;
    }

    public void setVolume(Double volume) {
        this.Volume = volume;
    }

    public Double getLast() {
        return Last;
    }

    public void setLast(Double last) {
        this.Last = last;
    }

    public Double getBaseVolume() {
        return BaseVolume;
    }

    public void setBaseVolume(Double baseVolume) {
        this.BaseVolume = baseVolume;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.TimeStamp = timeStamp;
    }

    public Double getBid() {
        return Bid;
    }

    public void setBid(Double bid) {
        this.Bid = bid;
    }

    public Double getAsk() {
        return Ask;
    }

    public void setAsk(Double ask) {
        this.Ask = ask;
    }

    public Integer getOpenBuyOrders() {
        return OpenBuyOrders;
    }

    public void setOpenBuyOrders(Integer openBuyOrders) {
        this.OpenBuyOrders = openBuyOrders;
    }

    public Integer getOpenSellOrders() {
        return OpenSellOrders;
    }

    public void setOpenSellOrders(Integer openSellOrders) {
        this.OpenSellOrders = openSellOrders;
    }

    public Double getPrevDay() {
        return PrevDay;
    }

    public void setPrevDay(Double prevDay) {
        this.PrevDay = prevDay;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        this.Created = created;
    }
}
