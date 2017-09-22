package com.jlarrieux.bittrexbot.simulation.TO;

public class Sell {

    private Double Quantity;
    private Double Rate;

    public Sell(Double quantity, Double rate) {
        this.Quantity = quantity;
        this.Rate = rate;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        this.Quantity = quantity;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        this.Rate = rate;
    }

}
