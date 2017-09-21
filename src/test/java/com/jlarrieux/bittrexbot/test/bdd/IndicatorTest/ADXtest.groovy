package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.ADX
import com.jlarrieux.bittrexbot.TestUtil.TestData
import spock.lang.Shared
import spock.lang.Specification

class ADXtest extends  Specification{

    static int period = TestData.ADXdata.period;
    static double[] close = TestData.ADXdata.close
    static double[] high = TestData.ADXdata.high
    static double[] low = TestData.ADXdata.low
    static double[] trueRange = TestData.ADXdata.trueRange
    static double[] dmPlus = TestData.ADXdata.dmPlus
    static double[] dmMinus = TestData.ADXdata.dmMinus
    static double[] diPlus = TestData.ADXdata.diPlus
    static double[] diMinus = TestData.ADXdata.diMinus
    static double[] dx = TestData.ADXdata.dx
    static double[] adxData = TestData.ADXdata.adx
    static double[] trPeriod =TestData.ADXdata.trPeriod
    static int firstLayer = period/2


    @Shared ADX adx

    def setup(){
        adx = new ADX(period)
    }


    def"True Range check"(){
        when:
        adx.update(high[0], low[0],close[0])

        then:
        (close.length-1).times {n->
            adx.update(high[n+1], low[n+1],close[n+1])
            assert DoubleMath.fuzzyEquals(adx.getCurrentTrueRange(),trueRange[n], 0.0001 )
        }
    }

    def "DMplus check"(){
        when:
        adx.update(high[0], low[0],close[0])

        then:
        (close.length-1).times {n->
            adx.update(high[n+1], low[n+1],close[n+1])
            assert DoubleMath.fuzzyEquals(adx.getDMplus(),dmPlus[n], 0.0001 )
        }

    }


    def "DMminus check"(){
        when:
        adx.update(high[0], low[0],close[0])

        then:
        (close.length-1).times {n->
            adx.update(high[n+1], low[n+1],close[n+1])
            assert DoubleMath.fuzzyEquals(adx.getDMminus(),dmMinus[n], 0.0001 )
        }

    }


    def "trPeriod check"(){
        when:
        for(int i=0; i<firstLayer; i++) adx.update(high[i],low[i], close[i])


        then:
        (trPeriod.length).times {n->
            adx.update(high[firstLayer+n], low[firstLayer+n], close[firstLayer+n])
//            System.out.println(String.format("adx.trPeriod: %f\ttrperiod[i]: %f\tn:%d\ttrueRange: %f\tDMplus: %f\t adx.counter: %d\n\n", adx.getTrPeriod(), trPeriod[n], n, adx.getCurrentTrueRange(), adx.getDMplus(), adx.getCounter()))
            assert DoubleMath.fuzzyEquals(adx.getTrPeriod(),trPeriod[n],0.001)
        }

    }

    def "DI plus check"(){
        when:
        for(int i=0; i<firstLayer; i++) adx.update(high[i], low[i], close[i])


        then:
        (diPlus.length).times {n->
            int j = n + firstLayer
            adx.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(adx.getDIplus(), diPlus[n], 0.001)
        }

    }

    def "DI minus check"(){
        when:
        for(int i=0; i<firstLayer; i++) adx.update(high[i], low[i], close[i])


        then:
        (diMinus.length).times {n->
            int j = n + firstLayer
            adx.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(adx.getDIminus(), diMinus[n], 0.001)
        }

    }

    def "Direction check"(){
        when:
        adx.setDIminus(diMinus[0])
        adx.setDIplus(diPlus[0])

        then:
        assert adx.getADXDirection()<0
    }

    def "DX check"(){
        when:
        for(int i=0; i<firstLayer; i++) adx.update(high[i], low[i], close[i])


        then:
        (dx.length).times {n->
            int j = n + firstLayer
//            System.out.print(String.format("before running: counter: %d\n", adx.counter))
            adx.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(adx.getDX(), dx[n], 0.001)
        }

    }


    def"ADX check"(){
        when:
        for(int i=0; i<period-1; i++) adx.update(high[i], low[i], close[i])

        then:
        (adxData.length).times {n->
            int j = n + period-1;
            adx.update(high[j], low[j], close[j])
//            System.out.print(String.format("Adx check!!!: adx: %f\t dx: %f\nDxQ: %s", adx.getCurrentADX(), adx.getDX(), adx.getDXqueueAsString() ))
            assert DoubleMath.fuzzyEquals(adx.getCurrentADX(), adxData[n], 0.001)
        }
    }






}
