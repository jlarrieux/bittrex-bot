package com.jlarrieux.bittrexbot.simulation.TO;

public class Buy {

    private Double quantity;
    private Double rate;

    public Buy(Double quantity, Double rate) {
        this.quantity = quantity;
        this.rate = rate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
