package com.jlarrieux.bittrexbot.Entity;



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


//    public static class PositionPandLComparatorDescending implements Comparator<Position>{
//
//
//        @Override
//        public int compare(Position o1, Position o2) {
//            return Double.compare(o2.getPAndL(), o1.getPAndL());
//        }
//    }


}
