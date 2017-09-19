package com.jlarrieux.bittrexbot.simulation.TO;

import java.util.ArrayList;
import java.util.List;

public class MarketOrderBookTO {

    private Boolean success;
    private String message;
    private Result result;

    public MarketOrderBookTO(){
        success = true;
        message="";
        result = new Result();
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void createNaddBuy(Double quantity, Double rate) {
        Buy buy = new Buy(quantity, rate);
        result.addBuy(buy);
    }

    public void createNaddSell(Double quantity, Double rate) {
        Sell sell = new Sell(quantity, rate);
        result.addSell(sell);
    }

    public class Result {

        private List<Buy> buy = new ArrayList<>();
        private List<Sell> sell = new ArrayList<>();

        public List<Buy> getBuy() {
            return buy;
        }

        public void setBuy(List<Buy> buy) {
            this.buy = buy;
        }

        public List<Sell> getSell() {
            return sell;
        }

        public void setSell(List<Sell> sell) {
            this.sell = sell;
        }

        public void addBuy(Buy buyElement) {
            if (buyElement != null) {
                buy.add(buyElement);
            }
        }

        public void addSell(Sell sellElement) {
            if (sellElement != null) {
                sell.add(sellElement);
            }
        }



    }

}
