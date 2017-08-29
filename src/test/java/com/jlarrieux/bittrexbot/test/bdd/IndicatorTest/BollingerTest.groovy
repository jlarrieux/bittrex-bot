package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.BollingerIndicator
import com.jlarrieux.bittrexbot.TestUtil.TestData
import spock.lang.Shared
import spock.lang.Specification

class BollingerTest extends Specification {


    static double[] price =TestData.BollingerData.price
    static double[] high = TestData.BollingerData.high
    static double[] mid = TestData.BollingerData.mid
    static double[] low = TestData.BollingerData.low
    static double[] bandwidth = TestData.BollingerData.bandwidth
    static double[] percentB = TestData.BollingerData.percentB
    static int period = TestData.BollingerData.period


    @Shared BollingerIndicator bollingerIndicator;


    def setup(){
        bollingerIndicator = new BollingerIndicator(period)
        for(int i=0; i<period-1; i++) bollingerIndicator.update(price[i])
    }



    def "Low bollinger check"(){
        expect:
        (low.length).times {n->
            bollingerIndicator.update(price[n+period-1])
            assert DoubleMath.fuzzyEquals(bollingerIndicator.getLow(), low[n], 0.0001)
        }
    }




    def"Mid bollinger check"(){
        expect:
        (mid.length).times {n->
            bollingerIndicator.update(price[n+period-1])
            assert DoubleMath.fuzzyEquals(bollingerIndicator.getMid(), mid[n], 0.0001)
        }
    }

    def"High bollinger check"(){
        expect:
        (high.length).times {n->
            bollingerIndicator.update(price[n+period-1])
            assert DoubleMath.fuzzyEquals(bollingerIndicator.getHigh(), high[n], 0.0001)
        }
    }

    def"bandwidth bollinger check"(){
        expect:
        (bandwidth.length).times {n->
            bollingerIndicator.update(price[n+period-1])
            assert DoubleMath.fuzzyEquals(bollingerIndicator.getBandwidth(), bandwidth[n], 0.0001)
        }
    }

    def"Percent B bollinger check"(){
        expect:
        (percentB.length).times {n->
            bollingerIndicator.update(price[n+period-1])
            assert DoubleMath.fuzzyEquals(bollingerIndicator.percentB, percentB[n], 0.0001)
        }
    }








}
