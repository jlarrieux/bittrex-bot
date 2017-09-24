package com.jlarrieux.bittrexbot.simulation.TO;

import java.util.ArrayList;
import java.util.List;

public class MarketSummaryTO {

    private Boolean success = true;
    private String message = "";
    private List<Result> result = new ArrayList<>();

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

        private String MarketName;
        private Double High;
        private Double Low;
        private Double Volume;
        private Double Last;
        private String timeStamp;
        private Double Bid;
        private Double Ask;
        private Integer OpenBuyOrders;
        private Integer OpenSellOrders;
        private Double PrevDay;


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

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
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
    }

}
