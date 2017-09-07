package com.jlarrieux.bittrexbot.UseCaseLayer;



import com.jlarrieux.bittrexbot.Entity.Market;

import java.util.Comparator;



public class Comparators {


    public static class MarketRsiComparatorAscending implements Comparator<Market>{


        @Override
        public int compare(Market o1, Market o2) {
            return Double.compare(o1.getCurrentRSI(),o2.getCurrentRSI());
        }
    }



    public static class MarketRsiComparatorDescending implements Comparator<Market>{
        @Override
        public int compare(Market o1, Market o2) {
            return Double.compare(o2.getCurrentRSI(),o1.getCurrentRSI());
        }
    }



    public static class MarketKeltnerToLastPriceAscending implements Comparator<Market>{
        @Override
        public int compare(Market m1, Market m2) {
            double c1 = m1.getLast() - m1.getKeltnerChannels().getHigh();
            double c2 = m2.getLast() - m2.getKeltnerChannels().getHigh();
            return Double.compare(c1,c2);
        }
    }


}
