package com.jlarrieux.bittrexbot.Entity;



import com.jlarrieux.bittrexbot.Util.IndicatorUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Log
@Data
public class KeltnerChannels {

    double previousAverageTrueRange=-1, previousMid=-1;
    double mid, high, low, averageTrueRange;
    DescriptiveStatistics priceQueue = new DescriptiveStatistics();
    DescriptiveStatistics trueRangeQueue = new DescriptiveStatistics();
    int period;
    int counter=-1;
    double previousPrice =-1;

    public KeltnerChannels(int period){
        setWindowSize(period);
    }

    public void setWindowSize(int period){
        this.period = period;
        priceQueue.setWindowSize(period);
        trueRangeQueue.setWindowSize(period);
    }

    public void update(double high, double low, double price){

        priceQueue.addValue(price);
        double trueRange = IndicatorUtil.calculateTrueRange(high, low, previousPrice);
//        log.info(String.format("~~Calc!!!\t high: %f\t low: %f\t price: %f\t previousTrueRange: %f\t trueRange: %f\n\n", high, low, price, previousPrice, trueRange));
        trueRangeQueue.addValue(trueRange);
        if(priceQueue.getN()>= priceQueue.getWindowSize()) calculateKeltner();
        previousPrice = price;


    }


    private void calculateKeltner(){
        averageTrueRange = IndicatorUtil.calculateATR(trueRangeQueue, previousAverageTrueRange);
        mid = IndicatorUtil.calculateEMA(priceQueue, previousMid);
        high = mid+(averageTrueRange*2);
        low = mid- (averageTrueRange*2);
        previousAverageTrueRange = averageTrueRange;
        previousMid = mid;
    }






}
