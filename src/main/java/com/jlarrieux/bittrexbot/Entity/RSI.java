package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;



@Log
@Data
public class RSI {

    private double averageGain, previousAverageGain=-1, averageLoss, previousAverageLoss=-1, currentRSI, RS, currentPrice, currentGain, currentLoss;
    private DescriptiveStatistics gains , loss;
    private double[] prevPrice = new double[1];
    private int counter=0;


    public RSI(int period){
        gains = new DescriptiveStatistics(period);
        loss = new DescriptiveStatistics(period);
    }


    public void updateGainLoss(double price){
        currentPrice = price;
        if(counter>0) calculateGainLoss();
        prevPrice[0] = price;
        counter++;
        if(counter>=gains.getWindowSize()) calculateRSI();

    }

    private void calculateGainLoss(){
        double change = currentPrice - prevPrice[0];
        currentGain = 0;
        currentLoss = 0;
//        log.info(String.format("current priceQueue: %f\t prevPrice: %f\t change: %f\t counter: %d\n\n",currentPrice,prevPrice[0],change, counter ));
        if(change>0){
            gains.addValue(change);
            currentGain = change;
        }
        else if(change<0){
            loss.addValue(Math.abs(change));
            currentLoss = Math.abs(change);
        }
    }

    private void calculateRSI(){
        RSIaverageGain();
        RSIaverageLoss();
        RS = averageGain/averageLoss;
        currentRSI = 100-(100/(1+RS));

//        log.info(String.format("~~Calculating Current currentRSI: averageGain: %f\taverageLoss: %f\t rs: %f\t rsi: %f\n\n\n\n\n"               , averageGain, averageLoss, RS, currentRSI));

    }


    private void RSIaverageGain(){
        int period = gains.getWindowSize();
        if(previousAverageGain==-1) averageGain= gains.getSum()/period;
        else{

            averageGain = (previousAverageGain*(period-1)+currentGain)/period ;
//            log.info(String.format("~~Inside averagGain: previousGains: %f\tperiod: %d\tcurrentGain: %f\taverageGain: %f\n"             , previousAverageGain,period,currentGain,averageGain));
        }
        previousAverageGain = averageGain;
    }


    private void RSIaverageLoss(){
        int period = gains.getWindowSize();
        if(previousAverageLoss==-1) averageLoss = loss.getSum()/loss.getWindowSize();
        else{
            averageLoss = (previousAverageLoss*(period-1)+currentLoss)/period;
//            log.info(String.format("~~Inside averagLoss: previousLoss: %f\tperiod: %d\tcurrentLoss: %f\taverageLoss: %f\n"                 , previousAverageLoss,period,currentLoss,averageLoss));
        }
        previousAverageLoss = averageLoss;
    }


    public void setWindowSize(int window){
        gains.setWindowSize(window);
        loss.setWindowSize(window);
    }



}
