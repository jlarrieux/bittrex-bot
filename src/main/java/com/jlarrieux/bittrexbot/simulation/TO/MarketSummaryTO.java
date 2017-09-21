package com.jlarrieux.bittrexbot.simulation.TO;

import java.util.ArrayList;
import java.util.List;

public class MarketSummaryTO {

    private Boolean success;
    private String message;
    private List<Result> result = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result == null ? result = new ArrayList<>():result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public Result createResult(){
        return new Result();
    }
    /******************
    Result Class
    ******************/
     public class Result {

        private String marketName;
        private Double high;
        private Double low;
        private Double volume;
        private Double last;
        private String timeStamp;
        private Double bid;
        private Double ask;
        private Integer openBuyOrders;
        private Integer openSellOrders;
        private Double prevDay;


        public String getMarketName() {
            return marketName;
        }

        public void setMarketName(String marketName) {
            this.marketName = marketName;
        }

        public Double getHigh() {
            return high;
        }

        public void setHigh(Double high) {
            this.high = high;
        }

        public Double getLow() {
            return low;
        }

        public void setLow(Double low) {
            this.low = low;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        public Double getLast() {
            return last;
        }

        public void setLast(Double last) {
            this.last = last;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public Double getBid() {
            return bid;
        }

        public void setBid(Double bid) {
            this.bid = bid;
        }

        public Double getAsk() {
            return ask;
        }

        public void setAsk(Double ask) {
            this.ask = ask;
        }

        public Integer getOpenBuyOrders() {
            return openBuyOrders;
        }

        public void setOpenBuyOrders(Integer openBuyOrders) {
            this.openBuyOrders = openBuyOrders;
        }

        public Integer getOpenSellOrders() {
            return openSellOrders;
        }

        public void setOpenSellOrders(Integer openSellOrders) {
            this.openSellOrders = openSellOrders;
        }

        public Double getPrevDay() {
            return prevDay;
        }

        public void setPrevDay(Double prevDay) {
            this.prevDay = prevDay;
        }
    }

}
