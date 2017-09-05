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
        for(int i=0; i<k; i++)m.updatePrice(price[i])


        then:
        (sma.length).times {n->
            m.updatePrice(price[period-1+n])
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
        for(int i=0; i<k; i++)m.updatePrice(price[i])


        then:
        (ema.length).times {n->
            m.updatePrice(price[period-1+n])
            double old = -1
            if(n!=0) old = ema[n-1]
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
        for(int i=0; i<period-1; i++) m.updatePrice(close[i])

        then:
        (rsiData.length).times {n->
            m.updatePrice(close[n+period])
            assert DoubleMath.fuzzyEquals(m.getRsi().getCurrentRSI(), rsiData[n], 0.0001)
        }

    }


    def"Bollinger check High"(){
        given:
        int period = TestData.BollingerData.period
        m.getBollingerSMA().setWindowSize(period)
        double[] price =TestData.BollingerData.price
        double[] high = TestData.BollingerData.high

        when:
        for(int i=0; i<period-1; i++) m.updatePrice(price[i])

        then:
        (high.length).times {n->
            m.updatePrice(price[n+period-1])
            assert DoubleMath.fuzzyEquals(m.getBollingerSMA().getHigh(), high[n], 0.0001)
        }
    }


    def"Bollinger check mid"(){
        given:
        int period = TestData.BollingerData.period
        m.getBollingerSMA().setWindowSize(period)
        double[] price =TestData.BollingerData.price
        double[] mid = TestData.BollingerData.mid

        when:
        for(int i=0; i<period-1; i++) m.updatePrice(price[i])

        then:
        (mid.length).times {n->
            m.updatePrice(price[n+period-1])
            assert DoubleMath.fuzzyEquals(m.getBollingerSMA().getMid(), mid[n], 0.0001)
        }
    }



    def"Bollinger check Low"(){
        given:
        int period = TestData.BollingerData.period
        m.getBollingerSMA().setWindowSize(period)
        double[] price =TestData.BollingerData.price
        double[] low = TestData.BollingerData.low

        when:
        for(int i=0; i<period-1; i++) m.updatePrice(price[i])

        then:
        (low.length).times {n->
            m.updatePrice(price[n+period-1])
            assert DoubleMath.fuzzyEquals(m.getBollingerSMA().getLow(), low[n], 0.0001)
        }
    }


    def"Bollinger check Bandwidth"(){
        given:
        int period = TestData.BollingerData.period
        m.getBollingerSMA().setWindowSize(period)
        double[] price =TestData.BollingerData.price
        double[] bandWidth = TestData.BollingerData.bandwidth

        when:
        for(int i=0; i<period-1; i++) m.updatePrice(price[i])

        then:
        (bandWidth.length).times {n->
            m.updatePrice(price[n+period-1])
            assert DoubleMath.fuzzyEquals(m.getBollingerSMA().getBandwidth(), bandWidth[n], 0.0001)
        }
    }


    def"Bollinger check % B"(){
        given:
        int period = TestData.BollingerData.period
        m.getBollingerSMA().setWindowSize(period)
        double[] price =TestData.BollingerData.price
        double[] percentB = TestData.BollingerData.percentB

        when:
        for(int i=0; i<period-1; i++) m.updatePrice(price[i])

        then:
        (percentB.length).times {n->
            m.updatePrice(price[n+period-1])
            assert DoubleMath.fuzzyEquals(m.getBollingerSMA().getPercentB(), percentB[n], 0.0001)
        }
    }




    def "Average True Range market test"() {
        given: "A Market with definition"
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


    def "ADX market test"(){
        given:
        int period = TestData.ADXdata.period
        m.getAdx().setWindowSize(period)
        double[] close = TestData.ADXdata.close
        double[] high = TestData.ADXdata.high
        double[] low = TestData.ADXdata.low
        double[] adxData = TestData.ADXdata.adx

        when:
        for(int i=0 ;i<period-1;i++){
            m.setHigh(high[i])
            m.setLow(low[i])
            m.updatePrice(close[i])
        }

        then:
        (adxData.length).times {n->
            int j = n + period-1
            m.setHigh(high[j])
            m.setLow(low[j])
            m.updatePrice(close[j])
            assert DoubleMath.fuzzyEquals(m.adx.getCurrentADX(),adxData[n],0.001)
        }


    }



    def"Keltner High market test"(){
        given:
        int period = TestData.KeltnerChannel.period
        double[] close = TestData.KeltnerChannel.close
        double[] midKeltner = TestData.KeltnerChannel.midKeltner
        double[] highKeltner= TestData.KeltnerChannel.highKeltner
        double[] lowKeltner = TestData.KeltnerChannel.lowKeltner
        double[] high = TestData.KeltnerChannel.high
        double[] low = TestData.KeltnerChannel.low
        m.getKeltnerChannels().setWindowSize(period)
        for(int i=0; i<period-1; i++){
            m.setHigh(high[i])
            m.setLow(low[i])
            m.updatePrice(close[i])
        }


        expect:
        (highKeltner.length).times {n->
            int j= n+ period-1
            m.setHigh(high[j])
            m.setLow(low[j])
            m.updatePrice(close[j])

            assert DoubleMath.fuzzyEquals(m.getKeltnerChannels().getHigh(), highKeltner[n], 0.001)
        }

    }


    def"Keltner low market test"(){
        given:
        int period = TestData.KeltnerChannel.period
        double[] close = TestData.KeltnerChannel.close
        double[] midKeltner = TestData.KeltnerChannel.midKeltner
        double[] highKeltner= TestData.KeltnerChannel.highKeltner
        double[] lowKeltner = TestData.KeltnerChannel.lowKeltner
        double[] high = TestData.KeltnerChannel.high
        double[] low = TestData.KeltnerChannel.low
        m.getKeltnerChannels().setWindowSize(period)
        for(int i=0; i<period-1; i++){
            m.setHigh(high[i])
            m.setLow(low[i])
            m.updatePrice(close[i])
        }


        expect:
        (lowKeltner.length).times {n->
            int j= n+ period-1
            m.setHigh(high[j])
            m.setLow(low[j])
            m.updatePrice(close[j])
            assert DoubleMath.fuzzyEquals(m.getKeltnerChannels().getLow(), lowKeltner[n], 0.001)
        }

    }



    def"Keltner mid market test"(){
        given:
        int period = TestData.KeltnerChannel.period
        double[] close = TestData.KeltnerChannel.close
        double[] midKeltner = TestData.KeltnerChannel.midKeltner
        double[] highKeltner= TestData.KeltnerChannel.highKeltner
        double[] lowKeltner = TestData.KeltnerChannel.lowKeltner
        double[] high = TestData.KeltnerChannel.high
        double[] low = TestData.KeltnerChannel.low
        m.getKeltnerChannels().setWindowSize(period)
        for(int i=0; i<period-1; i++){
            m.setHigh(high[i])
            m.setLow(low[i])
            m.updatePrice(close[i])
        }


        expect:
        (midKeltner.length).times {n->
            int j= n+ period-1
            m.setHigh(high[j])
            m.setLow(low[j])
            m.updatePrice(close[j])
            assert DoubleMath.fuzzyEquals(m.getKeltnerChannels().getMid(), midKeltner[n], 0.001)
        }

    }












}
