package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.extern.java.Log;



@Log
@Data
public class MarketShell {

    String marketName, baseCurrency,marketCurrency;
    double high, low, bid, ask, volume, spread, last, minTradeSize;
    int openBuyOrders, openSellOrders;
    boolean isActive;

    public MarketShell(JsonObject object){
        JsonObject market = object.getAsJsonObject(Constants.upperCaseFirst(Constants.MARKET));
        JsonObject summary =object.getAsJsonObject(Constants.SUMMARY);
        buildMarket(market, summary);
    }

    private void buildMarket(JsonObject market, JsonObject summary){
        marketCurrency = JsonParserUtil.getStringFromJsonObject(market, Constants.MARKET_CURRENCY);
        baseCurrency = JsonParserUtil.getStringFromJsonObject(market, Constants.BASE_CURRENCY);
        marketName = JsonParserUtil.getStringFromJsonObject(market, Constants.MARKET_NAME);
        isActive = JsonParserUtil.getBooleanFromJsonObject(market, Constants.IS_ACTIVE);
        minTradeSize = JsonParserUtil.getDoubleFromJsonObject(market, Constants.MIN_TRADE_SIZE);

        marketName = JsonParserUtil.getStringFromJsonObject(summary, Constants.MARKET_NAME);
        high = JsonParserUtil.getDoubleFromJsonObject(summary, Constants.HIGH);
        low = JsonParserUtil.getDoubleFromJsonObject(summary,  Constants.LOW);
        last = JsonParserUtil.getDoubleFromJsonObject(summary, Constants.LAST);
        bid = JsonParserUtil.getDoubleFromJsonObject(summary, Constants.BID);
        ask = JsonParserUtil.getDoubleFromJsonObject(summary, Constants.ASK);
        openBuyOrders = JsonParserUtil.getIntFromJsonObject(summary, Constants.OPEN_BUY_ORDERS);
        openSellOrders = JsonParserUtil.getIntFromJsonObject(summary, Constants.OPEN_SELL_ORDERS);
        volume = JsonParserUtil.getDoubleFromJsonObject(summary, Constants.VOLUME);
        spread = 100*(ask-bid)/ask;

    }

    public Market getMarket(){
        Market m = new Market();
        m.setMarketName(marketName);
        m.setBaseCurrency(baseCurrency);
        m.setMarketCurrency(marketCurrency);
        m.setHigh(high);
        m.setLow(low);
        m.setBid(bid);
        m.setAsk(ask);
        m.setVolume(volume);
        m.setSpread(spread);
        m.setLast(last);
        m.setMinTradeSize(minTradeSize);
        m.setOpenBuyOrders(openBuyOrders);
        m.setOpenSellOrders(openSellOrders);
        m.setActive(isActive);
        return m;
    }

}
