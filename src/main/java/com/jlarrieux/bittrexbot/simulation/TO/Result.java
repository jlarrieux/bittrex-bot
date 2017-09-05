package com.jlarrieux.bittrexbot.simulation.TO;

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
