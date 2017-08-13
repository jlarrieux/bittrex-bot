package com.jlarrieux.bittrexbot.Util;



import com.jlarrieux.bittrexbot.Entity.BollingerIndicator;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


@Log
public class IndicatorUtil {

    public static BollingerIndicator calculateBollingerSMA(DescriptiveStatistics values){
        double average = calculateSMA(values);
        double stDev = values.getStandardDeviation();
        BollingerIndicator bollingerIndicator = new BollingerIndicator();
        bollingerIndicator.setMid(average);
        bollingerIndicator.setHigh(average+2*stDev);
        bollingerIndicator.setLow(average-2*stDev);
        bollingerIndicator.updateSecondary();
        return bollingerIndicator;
    }

    public static BollingerIndicator calculateBollingerEMA(DescriptiveStatistics values, double ema ){

        double stdev = values.getStandardDeviation();
        BollingerIndicator bo = new BollingerIndicator();
        bo.setMid(ema);
        bo.setHigh(ema + 2*stdev);
        bo.setLow(ema - 2*stdev);
        bo.updateSecondary();
        return bo;
    }

    public static Double calculateEMA(DescriptiveStatistics values, double oldEMA){
        double price = values.getElement(values.getWindowSize()-1);
        if(oldEMA ==0) return calculateSMA(values);
        else{
            double multiplier = 2/(values.getN()+1);
            return price*multiplier+ ((1-multiplier)*oldEMA);
        }

    }

    private static double calculateSMA(DescriptiveStatistics values){
        return values.getSum()/values.getN();
    }


    public static double calculateRSI(DescriptiveStatistics gainsQueue, DescriptiveStatistics lossQueue){
        double averageGain = gainsQueue.getSum()/ Constants.RSI_WINDOW;
        double averageLoss = lossQueue.getSum()/ Constants.RSI_WINDOW;
        double RS = averageGain/averageLoss;
        double RSI = 100-(100/(1+RS));
        return RSI;
    }

}
