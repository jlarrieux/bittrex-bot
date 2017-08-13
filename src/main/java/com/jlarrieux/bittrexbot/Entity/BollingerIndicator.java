package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;



@Data
public class BollingerIndicator {

    double high= Double.NaN, mid= Double.NaN, low= Double.NaN;
    double percentDifference = Double.NaN;
    double spread = Double.NaN;

    public void updateSecondary(){
        percentDifference = Math.abs(high -low)/((high+low)/2);;
        spread = high - low;
    }
}
