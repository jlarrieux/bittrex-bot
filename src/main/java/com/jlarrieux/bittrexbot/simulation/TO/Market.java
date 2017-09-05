package com.jlarrieux.bittrexbot.simulation.TO;

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
