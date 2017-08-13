package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.IndicatorUtil;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Component;



@Component
@Log
@Data
@ToString(exclude = {"summary", "market", "marketCurrency", "baseCurrency", "isActive", "priceQueue", "gainsQueue","lossQueue"})
@EqualsAndHashCode(exclude = {"summary", "market"})
public class Market {


    //    private  EvictingQueue priceQueue = EvictingQueue.create(10);

    private DescriptiveStatistics priceQueue = new DescriptiveStatistics(Constants.BOLLINGER_WINDOW);
    private DescriptiveStatistics gainsQueue = new DescriptiveStatistics(Constants.RSI_WINDOW);
    private DescriptiveStatistics lossQueue = new DescriptiveStatistics(Constants.RSI_WINDOW);

    private String marketCurrency,  baseCurrency, marketName;
    private boolean isActive;
    private double minTradeSize, high, low, volume, last, bid, ask, spread, RSI=-1;
    private double oldEMA=0;
    private int openBuyOrders, openSellOrders;
    private BollingerIndicator bollingerSMA, bollingerEMA;

    public Market(){

    }

    public Market(JsonObject object){
        JsonObject market = object.getAsJsonObject(Constants.upperCaseFirst(Constants.MARKET));
        JsonObject summary =object.getAsJsonObject(Constants.SUMMARY);
        bollingerSMA = new BollingerIndicator();
        bollingerEMA = new BollingerIndicator();
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

    public void updatePrice(double price){
        if(Double.compare(last, price)>0) lossQueue.addValue(Math.abs(price-last));
        else if(Double.compare(last, price)<0) gainsQueue.addValue(Math.abs(price-last));
        priceQueue.addValue(price);
        calculateIndicators();
    }

    private void calculateIndicators(){
        if(priceQueue.getN()==Constants.BOLLINGER_WINDOW) calculateBollinger();
        if(priceQueue.getN()==Constants.RSI_WINDOW) calculateRSI();
    }

    private void calculateBollinger(){
        bollingerSMA = IndicatorUtil.calculateBollingerSMA(priceQueue);
        oldEMA = IndicatorUtil.calculateEMA(priceQueue, oldEMA);
        bollingerEMA = IndicatorUtil.calculateBollingerEMA(priceQueue, oldEMA);
    }

    private void calculateRSI(){
        RSI = IndicatorUtil.calculateRSI(gainsQueue, lossQueue);
    }

    public String printQueueToString(){
        StringBuilder builder = new StringBuilder("[");
        for(Double d: priceQueue.getValues()){
            if(builder.length()>1) builder.append(", ");
            builder.append(d);
        }
        builder.append("]");
        return builder.toString();
    }


    public String bollingerToString(){
        return "SMA Bollinger: "+bollingerSMA.toString() + "\n EMA Bollinger: "+ bollingerEMA.toString()+String.format("\nBid: %f\tAsk: %f\tprice: %f\tspread: %.2f%%\t# of buys: %d\t # of sells: %d", bid, ask, last, spread, openBuyOrders, openSellOrders);
    }

    public String RSItoString(){
        return String.format("RSI: %.2f", RSI);
    }


}
