package com.jlarrieux.bittrexbot.Util;



import com.jlarrieux.bittrexbot.Entity.ADX;
import com.jlarrieux.bittrexbot.Entity.ADXhelper;
import com.jlarrieux.bittrexbot.Entity.BollingerIndicator;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Arrays;
import java.util.stream.Collectors;



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
        if(oldEMA ==-1) return calculateSMA(values);
        else{
            double multiplier = 2/(values.getN()+1);
            return price*multiplier+ ((1-multiplier)*oldEMA);
        }

    }

    public static double calculateSMA(DescriptiveStatistics values){
        log.info("values: "+ Constants.getListAsString(Arrays.stream(values.getValues()).boxed().collect(Collectors.toList())));
        return values.getMean();
    }





    public static ADX calculateADX(ADXhelper adXhelper){
        ADX adx = new ADX();
        double upMove = adXhelper.getCurrentHigh()- adXhelper.getPreviousHigh();
        double downMove = adXhelper.getPreviousLow() - adXhelper.getCurrentLow();
        if(upMove>downMove && upMove>0) adx.setDMplus(upMove);
        if(downMove>upMove && downMove>0) adx.setDMminus(downMove);



        return adx;
    }

    public static double calculateATR(DescriptiveStatistics TRholder, double oldATR){
        if(oldATR==-1)         return TRholder.getMean();
        else{
            double lastTR= TRholder.getElement((int) (TRholder.getN()-1));
            double numerator = oldATR*(TRholder.getN()-1)+lastTR;
            double result = numerator/TRholder.getN();
            log.info(String.format( "ATR----\t numerator: %f\tdenominator: %d\t\tresult: %f",numerator,TRholder.getN(),result) );
            return (numerator/TRholder.getN());
        }
    }


    public static double calculateTrueRange(double currentHigh, double currentLow, double previousPrice){
        double m1 = currentHigh - currentLow;
        double m2=0;
        double m3=0;
        if(previousPrice!=-1){
            m2 = Math.abs(currentHigh - previousPrice);
            m3 = Math.abs(currentLow - previousPrice);
        }

        double max = Math.max(Math.max(m1, m2), m3);
        log.info(String.format("high: %f\tlow: %f\tpp: %f\nm1: %f\tm2: %f\tm3: %f\t\tmax: %f\n\n", currentHigh, currentLow, previousPrice, m1, m2, m3, max));
        return max;


    }






}
