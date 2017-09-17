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
    private int openBuyOrders, openSellOrders;
    private BollingerIndicator bollingerSMA = new BollingerIndicator(Constants.DATA_WINDOW);
    private ADX adx = new ADX(Constants.DATA_WINDOW);
    private KeltnerChannels keltnerChannels = new KeltnerChannels(Constants.DATA_WINDOW);
    private double atr=-1;

    public Market(){

    }

    public Market(JsonObject object){
        JsonObject market = object.getAsJsonObject(Constants.upperCaseFirst(Constants.MARKET));
        JsonObject summary =object.getAsJsonObject(Constants.SUMMARY);
        bollingerSMA = new BollingerIndicator(Constants.DATA_WINDOW);
        adx.setWindowSize(Constants.DATA_WINDOW);
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
        adx.setMarketName(marketName);
        updatePrice(last);
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

    public void updatePrice(double price){

        rsi.updateGainLoss(price);
        priceQueue.addValue(price);
        ATRholder.addValue(IndicatorUtil.calculateTrueRange(high,low,last ));
        bollingerSMA.update(price);
        adx.update(high,low,price);
        keltnerChannels.update(high,low,price);
        calculateIndicators();
        last = price;
    }

    private void calculateIndicators(){
        if(priceQueue.getN()==Constants.DATA_WINDOW){
            currentRSI = rsi.getCurrentRSI();
            atr = IndicatorUtil.calculateATR(ATRholder,atr);

        }

    }



    public double getAdxValue(){
        return adx.getCurrentADX();
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
        return  "Bollinger: "+ bollingerSMA.toString()+String.format("\nBid: %f\tAsk: %f\tpriceQueue: %f\tspread: %.2f%%\t# of buys: %d\t # of sells: %d", bid, ask, last, spread, openBuyOrders, openSellOrders);
    }

    public String RSItoString(){
        return String.format("currentRSI: %.2f", currentRSI);
    }


    public void alternateConstruction(JsonObject object, boolean FromServer){
        setMarketCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_CURRENCY_SHORT));
        setBaseCurrency(JsonParserUtil.getStringFromJsonObject(object, Constants.BASE_CURRENCY_SHORT));
        setMarketName(JsonParserUtil.getStringFromJsonObject(object, Constants.MARKET_NAME_SHORT));
        setActive(JsonParserUtil.getBooleanFromJsonObject(object, Constants.ACTIVE));
        setMinTradeSize(JsonParserUtil.getDoubleFromJsonObject(object, Constants.MIN_TRADE_SIZE_SHORT));
        adx.setMarketName(marketName);
        if(FromServer) {
            JsonArray databook = object.getAsJsonArray(Constants.DATA_BOOK);
            iterateFromServerData(databook);
        }
    }


    private void iterateFromServerData(JsonArray jsonArray){
        JsonObject object = null;
        for(int i=0; i<jsonArray.size();i++){
            object = (JsonObject) jsonArray.get(i);
            double price = JsonParserUtil.getDoubleFromJsonObject(object, Constants.LAST.toLowerCase());
            if(i==0) setLast(price);
            else{
                setHigh(JsonParserUtil.getDoubleFromJsonObject(object,Constants.HIGH.toLowerCase()));
                setLow(JsonParserUtil.getDoubleFromJsonObject(object,Constants.LOW.toLowerCase()));
                setBid(JsonParserUtil.getDoubleFromJsonObject(object,Constants.BID.toLowerCase()));
                setAsk(JsonParserUtil.getDoubleFromJsonObject(object,Constants.ASK.toLowerCase()));
                setVolume(JsonParserUtil.getDoubleFromJsonObject(object,Constants.VOLUME.toLowerCase()));
                setSpread(JsonParserUtil.getDoubleFromJsonObject(object, Constants.SPREAD));
                setOpenBuyOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_BUY_ORDERS_SHORT));
                setOpenSellOrders(JsonParserUtil.getIntFromJsonObject(object, Constants.OPEN_SELL_ORDERS_SHORT));
                updatePrice(price);
            }
        }


    }

    public double getBollingerHigh(){
        return bollingerSMA.getHigh();
    }

    public double getBollingerLow(){
        return bollingerSMA.getLow();
    }

    public double getBollingerMid(){
        return bollingerSMA.getMid();
    }

    public double getBollingerBandwidth(){
        return bollingerSMA.getBandwidth();
    }

    public double getBollingerPercentB(){
        return bollingerSMA.getPercentB();
    }

    public boolean isPriceBelowKeltnerLow(){
        return last< keltnerChannels.getLow();
    }



    public boolean isPriceAboveKeltnerHigh(){
        return last> keltnerChannels.getHigh();
    }


}
