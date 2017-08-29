package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.KeltnerChannels
import com.jlarrieux.bittrexbot.TestUtil.TestData
import spock.lang.Shared
import spock.lang.Specification

class KeltnerChannelsTest  extends Specification{

    static int period = TestData.KeltnerChannel.period
    static double[] close = TestData.KeltnerChannel.close
    static double[] averageTrueRange = TestData.KeltnerChannel.averageTrueRange
    static double[] midKeltner = TestData.KeltnerChannel.midKeltner
    static double[] highKeltner= TestData.KeltnerChannel.highKeltner
    static double[] lowKeltner = TestData.KeltnerChannel.lowKeltner
    static double[] high = TestData.KeltnerChannel.high
    static double[] low = TestData.KeltnerChannel.low

    @Shared KeltnerChannels kc

    def setup(){

        kc = new KeltnerChannels(period)
        for(int i=0; i<period-1; i++) kc.update(high[i], low[i], close[i])
    }



    def"ATR check"(){



        expect:
        (averageTrueRange.length).times {n->
            int j=n+period-1
            kc.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(kc.getAverageTrueRange(), averageTrueRange[n], 0.0001)
        }

    }


    def"Mid keltner check"(){

        expect:
        (midKeltner.length).times {n->
            int j= n+ period-1
            kc.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(kc.getMid(), midKeltner[n], 0.001)
        }
    }

    def"High keltner check"(){
        expect:
        (highKeltner.length).times {n->
            int j= n+ period-1
            kc.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(kc.getHigh(), highKeltner[n], 0.001)
        }
    }

    def"Low keltner check"(){
        expect:
        (lowKeltner.length).times {n->
            int j= n+ period-1
            kc.update(high[j], low[j], close[j])
            assert DoubleMath.fuzzyEquals(kc.getLow(), lowKeltner[n], 0.001)
        }
    }





}
