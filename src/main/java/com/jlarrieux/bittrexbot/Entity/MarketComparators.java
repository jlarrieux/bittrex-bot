package com.jlarrieux.bittrexbot.Entity;



import java.util.Comparator;



public class MarketComparators {


    public static class MarketRsiComparatorAscending implements Comparator<Market>{


        @Override
        public int compare(Market o1, Market o2) {
            return Double.compare(o1.getRSI(),o2.getRSI());
        }
    }

    public static class MarketRsiComparatorDescending implements Comparator<Market>{


        @Override
        public int compare(Market o1, Market o2) {
            return Double.compare(o2.getRSI(),o1.getRSI());
        }
    }


}
