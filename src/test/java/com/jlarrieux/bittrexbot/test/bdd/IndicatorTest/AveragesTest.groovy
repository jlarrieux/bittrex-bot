package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.TestUtil.TestData
import com.jlarrieux.bittrexbot.Util.IndicatorUtil
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import spock.lang.Shared
import spock.lang.Specification

class AveragesTest extends Specification{

    static double[] price = TestData.AverageData.price;
    static double[] sma = TestData.AverageData.sma;
    static double[] ema =TestData.AverageData.ema;
    static int period = TestData.AverageData.period;

    @Shared DescriptiveStatistics holder;

    def setup(){
        holder = new DescriptiveStatistics(period)
        for(int i=0; i<period-1; i++)holder.addValue(price[i])
    }

    def "Simple Moving Average check"(){

        expect:
        (sma.length).times {n->
            holder.addValue(price[period-1+n])
            assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateSMA(holder), sma[n], 0.01)
        }
    }


    def"Exponentianl Moving Average check"(){
        expect:
        (ema.length).times {n->
            holder.addValue(price[period-1+n])
            double old = -1
            if(n!=0) old = ema[n-1]
            assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateEMA(holder, old), ema[n], 0.01)
        }


    }



}
