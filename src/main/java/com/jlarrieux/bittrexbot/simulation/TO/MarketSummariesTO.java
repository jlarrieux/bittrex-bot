package com.jlarrieux.bittrexbot.simulation.TO;

import java.util.ArrayList;
import java.util.List;

public class MarketSummariesTO {

    private Boolean success =true;
    private String message = "";
    private List<Result> result = new ArrayList<>();

    public Summary createSummary(){
        return new Summary();
    }

    public Result createResult(){
        return new Result();
    }

    public Market createMarket(){
        return new Market();
    }
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
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    /*****************************
     * Class Result
     *****************************/
    public class Result {

        private Market Market;
        private Summary Summary;
        private Boolean IsVerified;


        public Market getMarket() {
            return Market;
        }

        public void setMarket(Market market) {
            this.Market = market;
        }

        public Summary getSummary() {
            return Summary;
        }

        public void setSummary(Summary summary) {
            this.Summary = summary;
        }

        public Boolean getIsVerified() {
            return IsVerified;
        }

        public void setIsVerified(Boolean isVerified) {
            this.IsVerified = isVerified;
        }
    }


    /*****************************
     * Class Summary
     *****************************/

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

        public Summary(){

        }

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

    /*****************************
     * Class Market
     *****************************/
    public class Market {

        private String MarketCurrency;
        private String BaseCurrency;
        private String MarketCurrencyLong;
        private String BaseCurrencyLong;
        private Double MinTradeSize;
        private String MarketName;
        private Boolean IsActive;
        private String Created;
        private Object Notice;
        private Object IsSponsored;
        private String LogoUrl;

        public String getMarketCurrency() {
            return MarketCurrency;
        }

        public void setMarketCurrency(String marketCurrency) {
            this.MarketCurrency = marketCurrency;
        }

        public String getBaseCurrency() {
            return BaseCurrency;
        }

        public void setBaseCurrency(String baseCurrency) {
            this.BaseCurrency = baseCurrency;
        }

        public String getMarketCurrencyLong() {
            return MarketCurrencyLong;
        }

        public void setMarketCurrencyLong(String marketCurrencyLong) {
            this.MarketCurrencyLong = marketCurrencyLong;
        }

        public String getBaseCurrencyLong() {
            return BaseCurrencyLong;
        }

        public void setBaseCurrencyLong(String baseCurrencyLong) {
            this.BaseCurrencyLong = baseCurrencyLong;
        }

        public Double getMinTradeSize() {
            return MinTradeSize;
        }

        public void setMinTradeSize(Double minTradeSize) {
            this.MinTradeSize = minTradeSize;
        }

        public String getMarketName() {
            return MarketName;
        }

        public void setMarketName(String marketName) {
            this.MarketName = marketName;
        }

        public Boolean getIsActive() {
            return IsActive;
        }

        public void setIsActive(Boolean isActive) {
            this.IsActive = isActive;
        }

        public String getCreated() {
            return Created;
        }

        public void setCreated(String created) {
            this.Created = created;
        }

        public Object getNotice() {
            return Notice;
        }

        public void setNotice(Object notice) {
            this.Notice = notice;
        }

        public Object getIsSponsored() {
            return IsSponsored;
        }

        public void setIsSponsored(Object isSponsored) {
            this.IsSponsored = isSponsored;
        }

        public String getLogoUrl() {
            return LogoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.LogoUrl = logoUrl;
        }
    }

}
