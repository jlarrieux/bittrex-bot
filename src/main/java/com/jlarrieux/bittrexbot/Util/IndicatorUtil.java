package com.jlarrieux.bittrexbot.Util;



import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;



@Log
public class IndicatorUtil {





    public static Double calculateEMA(DescriptiveStatistics values, double oldEMA){
        double price = values.getElement(values.getWindowSize()-1);
//        log.info(String.format("\n\nEMA-1~~~~price: %f\told Ema: %f\n", price, oldEMA));
        if(oldEMA ==-1){
            double result = calculateSMA(values);
//            log.info(String.format("EMA-2\t result: %f\n", result));
            return result;
        }
        else{
            double multiplier = (double)2/(values.getN()+1);
            double result = price*multiplier+ ((1-multiplier)*oldEMA);
//            log.info(String.format("EMA-3~~ old ema = %f\t price: %f\t ema: %f\tmultiplier: %f\t n: %d\n\n", oldEMA, price, result, multiplier, values.getN()));
            return result;
        }

    }

    public static double calculateSMA(DescriptiveStatistics values){
//        log.info("values: "+ Constants.getListAsString(Arrays.stream(values.getValues()).boxed().collect(Collectors.toList())));
        return values.getMean();
    }



    public static double calculateATR(DescriptiveStatistics TRholder, double oldATR){
        if(oldATR==-1 || Double.isNaN(oldATR))         return TRholder.getMean();
        else{
            double lastTR= TRholder.getElement((int) (TRholder.getN()-1));
            double numerator = oldATR*(TRholder.getN()-1)+lastTR;
            double result = numerator/TRholder.getN();
//            log.info(String.format( "ATR----\t numerator: %f\tdenominator: %d\t\tresult: %f",numerator,TRholder.getN(),result) );
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
//        log.info(String.format("high: %f\tlow: %f\tpp: %f\nm1: %f\tm2: %f\tm3: %f\t\tmax: %f\n\n", currentHigh, currentLow, previousPrice, m1, m2, m3, max));
        return max;


    }






}
