package com.jlarrieux.bittrexbot.Entity;



import com.google.gson.JsonArray;
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
@ToString(exclude = {"summary", "market", "marketCurrency", "baseCurrency", "isActive", "priceQueue", "rsi"})
@EqualsAndHashCode(exclude = {"summary", "market"})
public class Market {


    private DescriptiveStatistics priceQueue = new DescriptiveStatistics(Constants.DATA_WINDOW);
    private DescriptiveStatistics ATRholder = new DescriptiveStatistics(Constants.DATA_WINDOW);
    private RSI rsi = new RSI(Constants.DATA_WINDOW);

    private String marketCurrency,  baseCurrency, marketName;
    private boolean isActive;
    private double minTradeSize, high, low, volume, last, bid, ask, spread, currentRSI =-1;
    private double oldEMA=-1;
    private int openBuyOrders, openSellOrders;
    private BollingerIndicator bollingerSMA, bollingerEMA;
    private double atr=-1;

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
        rsi.updateGainLoss(last);
    }

    public void update(Market market){
        setHigh(market.getHigh());
        setLow(market.getLow());
        setBid(market.getBid());
        setAsk(market.getAsk());
        setVolume(market.getVolume());
        setSpread(market.getSpread());
        setOpenSellOrders(market.getOpenSellOrders());
        setOpenBuyOrders(market.getOpenBuyOrders());
        updatePrice(market.getLast());
    }

    private void updatePrice(double price){

        rsi.updateGainLoss(price);
        priceQueue.addValue(price);
        ATRholder.addValue(IndicatorUtil.calculateTrueRange(high,low,last ));
        calculateIndicators();
        last = price;
    }

    private void calculateIndicators(){
        if(priceQueue.getN()==Constants.DATA_WINDOW){
            currentRSI = rsi.getCurrentRSI();
            atr = IndicatorUtil.calculateATR(ATRholder,atr);
            oldEMA = IndicatorUtil.calculateEMA(priceQueue, oldEMA);
            bollingerSMA = IndicatorUtil.calculateBollingerSMA(priceQueue);
            bollingerEMA = IndicatorUtil.calculateBollingerEMA(priceQueue, oldEMA);
        }

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
        return  "EMA Bollinger: "+ bollingerEMA.toString()+String.format("\nBid: %f\tAsk: %f\tprice: %f\tspread: %.2f%%\t# of buys: %d\t # of sells: %d", bid, ask, last, spread, openBuyOrders, openSellOrders);
    }

    public String RSItoString(){
        return String.format("currentRSI: %.2f", currentRSI);
    }


    public void alternateConstruction(JsonObject object){
        setMarketCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_CURRENCY_SHORT));
        setBaseCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.BASE_CURRENCY_SHORT));
        setMarketName(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_NAME_SHORT));
        setActive(JsonParserUtil.getBooleanFromJsonObject(object, Constants.ACTIVE));
        setMinTradeSize(JsonParserUtil.getDoubleFromJsonObject(object, Constants.MIN_TRADE_SIZE_SHORT));
        JsonArray databook = object.getAsJsonArray(Constants.DATA_BOOK);
        iterateFromServerData(databook);
    }


    private void iterateFromServerData(JsonArray jsonArray){
        JsonObject object = null;
        for(int i=0; i<jsonArray.size();i++){
            object = (JsonObject) jsonArray.get(i);
            double price = JsonParserUtil.getDoubleFromJsonObject(object, Constants.LAST.toLowerCase());
            if(i==0) setLast(price);
            else updatePrice(price);
        }
        setHigh(JsonParserUtil.getDoubleFromJsonObject(object,Constants.HIGH.toLowerCase()));
        setLow(JsonParserUtil.getDoubleFromJsonObject(object,Constants.LOW.toLowerCase()));
        setBid(JsonParserUtil.getDoubleFromJsonObject(object,Constants.BID.toLowerCase()));
        setAsk(JsonParserUtil.getDoubleFromJsonObject(object,Constants.ASK.toLowerCase()));
        setVolume(JsonParserUtil.getDoubleFromJsonObject(object,Constants.VOLUME.toLowerCase()));
        setSpread(JsonParserUtil.getDoubleFromJsonObject(object, Constants.SPREAD));
        setOpenBuyOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_BUY_ORDERS_SHORT));
        setOpenSellOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_SELL_ORDERS_SHORT));

    }

}
