package com.jlarrieux.bittrexbot.Entity;



import lombok.Data;



@Data
public class ADXhelper {

    private double previousHigh, currentHigh, previousLow, currentLow;


    public static class Builder{
        private double previousHigh, currentHigh, previousLow, currentLow;
        public Builder previousHigh(double val){
            previousHigh = val;
            return this;
        }

        public Builder currentHigh(double val){
            currentHigh = val;
            return  this;
        }

        public Builder previousLow(double val){
            previousLow = val;
            return this;
        }

        public Builder currentLow(double val){
            currentLow = val;
            return  this;
        }

        public ADXhelper build(){
            return new ADXhelper(this);
        }
    }

    private ADXhelper(){

    }


    private ADXhelper(Builder builder){

    }



}
