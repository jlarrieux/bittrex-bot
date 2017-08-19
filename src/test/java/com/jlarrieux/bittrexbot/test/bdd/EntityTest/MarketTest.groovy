package com.jlarrieux.bittrexbot.test.bdd.EntityTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.Market
import com.jlarrieux.bittrexbot.TestUtil.TestData
import com.jlarrieux.bittrexbot.Util.IndicatorUtil
import spock.lang.Shared
import spock.lang.Specification

class MarketTest extends Specification {

    @Shared Market m
    def setup(){
        m = new Market()
    }


    def "SMA market check"(){
        given:
        int period = TestData.AverageData.period
        m.getPriceQueue().setWindowSize(period)
        int k = period -1
        double[] price = TestData.AverageData.price;
        double[] sma = TestData.AverageData.sma;

        when:
        for(int i=0; i<k; i++)m.getPriceQueue().addValue(price[i])


        then:
        (sma.length).times {n->
            m.getPriceQueue().addValue(price[period-1+n])
            assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateSMA(m.getPriceQueue()), sma[n], 0.01)
        }


    }



    def"EMA market check"(){
        given:
        int period = TestData.AverageData.period
        m.getPriceQueue().setWindowSize(period)
        int k = period -1
        double[] price = TestData.AverageData.price;
        double[] ema =TestData.AverageData.ema;


        when:
        for(int i=0; i<k; i++)m.getPriceQueue().addValue(price[i])


        then:
        (ema.length).times {n->
            m.getPriceQueue().addValue(price[period-1+n])
            double old = -1
            if(n!=0) old = ema[n]
            assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateEMA(m.getPriceQueue(), old), ema[n], 0.01)
        }


    }


    def"RSI market check"(){
        given:
        int period = TestData.RSIdata.period
        m.getRsi().setWindowSize(period)
        double[] close = TestData.RSIdata.close;
        double [] rsiData = TestData.RSIdata.RSI14;

        when:
        for(int i=0; i<period-1; i++) m.getRsi().updateGainLoss(close[i])

        then:
        (rsiData.length).times {n->
            m.getRsi().updateGainLoss(close[n+period])
            assert DoubleMath.fuzzyEquals(m.getRsi().getCurrentRSI(), rsiData[n], 0.0001)
        }

    }




    def "Average True Range market test"() {
        given: "A market with definition"
        int period = TestData.ATRdata.period
        m.getATRholder().setWindowSize(period)
        int k = period-1
        double[] highs = TestData.ATRdata.highs;
        double[] low = TestData.ATRdata.low;
        double[] close = TestData.ATRdata.close;
        double[] ATR = TestData.ATRdata.ATR;


        when: "we populate atr holder"
        for (int i = 0; i < k; i++) {
            int j = i - 1;
            double c = -1
            if (j > -1) c = close[j]
            m.getATRholder().addValue(IndicatorUtil.calculateTrueRange(highs[i], low[i], c))
        }



        then: "we should have correct calculations"
        (ATR.length).times { n ->
            m.getATRholder().addValue(IndicatorUtil.calculateTrueRange(highs[k + n], low[k + n], close[k + n - 1]))
            double c2 = -1
            if (n > 0) c2 = ATR[n - 1]
            assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateATR(m.getATRholder(), c2), ATR[n], 0.01)
        }
    }

















}
