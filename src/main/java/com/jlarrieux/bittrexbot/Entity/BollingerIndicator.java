package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;



@Log
@Data
public class BollingerIndicator {

    private double high= Double.NaN, mid= Double.NaN, low= Double.NaN;
    private double percentB = Double.NaN;
    private double  Bandwidth, currentPrice;
    private DescriptiveStatistics priceQueue = new DescriptiveStatistics();



    public BollingerIndicator(int period){
        priceQueue = new DescriptiveStatistics(period);
    }


    public void update(double price){
        currentPrice = price;
        priceQueue.addValue(price);

        if(priceQueue.getN()>=priceQueue.getWindowSize()){
            calculateBollinger();
            updateSecondary();
        }



    }



    private void updateSecondary(){
        percentB = (currentPrice-low)/(high-low);
        Bandwidth =100*(high-low)/mid;
//        log.info(String.format("Bandwidth: %f\t\tpercentB: %f\n\n", Bandwidth, percentB));

    }


    private void calculateBollinger(){
        double average = priceQueue.getMean();
        double stDev = calculateWholePopulationStandardDeviation();
        mid = average;
        high = average+2*stDev;
        low = average-2*stDev;

//        log.info(String.format("average: %f\tstdev: %f\tlow: %f\nvariancePop: %f, stdevPa: %f\n\n\n", average, stDev, low, variancePA,stDevPa));
    }

    private double calculateWholePopulationStandardDeviation(){
        return Math.sqrt(priceQueue.getPopulationVariance());
    }



    public void setWindowSize(int period){
        priceQueue.setWindowSize(period);
    }






}
