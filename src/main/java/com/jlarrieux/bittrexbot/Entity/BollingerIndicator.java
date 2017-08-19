package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;



@Data
public class BollingerIndicator {

    private double high= Double.NaN, mid= Double.NaN, low= Double.NaN;
    private double percentDifference = Double.NaN;
    private double spread = Double.NaN;
    private double bLowThreshold, bHighThreshold;

    public void updateSecondary(){
        percentDifference = Math.abs(high -low)/((high+low)/2);;
        spread = high - low;
        bLowThreshold = (mid+low)/2;
        bHighThreshold = (mid+high)/2;
    }
}
