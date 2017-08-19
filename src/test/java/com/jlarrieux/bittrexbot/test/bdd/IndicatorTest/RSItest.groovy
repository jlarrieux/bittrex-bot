package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.RSI
import com.jlarrieux.bittrexbot.TestUtil.TestData
import spock.lang.Specification

class RSItest extends Specification{

    static double[] close = TestData.RSIdata.close;
    static double [] rsiData = TestData.RSIdata.RSI14;
    static int period = TestData.RSIdata.period;



    def"RSI test"(){
        given: "A rsi"
        RSI rsi = new RSI(period)


        when: "We update the currentRSI"
        for(int i=0; i<period-1; i++)rsi.updateGainLoss(close[i])


        then:
        (rsiData.length).times{ n->
            rsi.updateGainLoss(close[n+period])
            assert DoubleMath.fuzzyEquals(rsi.getCurrentRSI(), rsiData[n], 0.0001)

        }
    }


}
