package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;



@Data
public class BollingerIndicator {

    double high= Double.NaN, mid= Double.NaN, low= Double.NaN;
    double percentDifference = Math.abs(high -low)/((high+low)/2);
    double spread = high - low;

}
