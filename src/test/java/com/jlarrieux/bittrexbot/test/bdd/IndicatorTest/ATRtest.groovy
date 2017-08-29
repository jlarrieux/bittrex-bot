package com.jlarrieux.bittrexbot.test.bdd.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.TestUtil.TestData
import com.jlarrieux.bittrexbot.Util.IndicatorUtil
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import spock.lang.Specification
import spock.lang.Unroll

class ATRtest extends Specification {


    static double[] highs = TestData.ATRdata.highs;
    static double[] low = TestData.ATRdata.low;
    static double[] close = TestData.ATRdata.close;
    static double[] trueRange = TestData.ATRdata.trueRange;
    static double[] ATR = TestData.ATRdata.ATR;
    static int period =TestData.ATRdata.period;

    def "same lenght"(){
        assert highs.length == low.length
        assert  low.length == close.length
    }





    @Unroll
    def "True Range checking"(){
        given:
        double[] y = new double[low.length]
        int j ;

        when:
        for(int i=0; i<low.length;i++){
            j=i-1
            double c =-1;
            if(j>=0) c= close[j]
            y[i]=IndicatorUtil.calculateTrueRange(highs[i], low[i], c)
        }


        then:
        (low.length).times {n->
            assert  DoubleMath.fuzzyEquals(trueRange[n],y[n], 0.01)
        }

    }


    @Unroll
    def "Correct calculation of Average True Range"(){
        given: "A ATRholder descriptiveStatistics q"
        DescriptiveStatistics atrHolder = new DescriptiveStatistics(period);
        int k=period-1;

        when: "Populated with the following data"
        for(int i=0; i<k; i++){
            int j=i-1;
            double c = -1;
            if(j>=0) c = close[j]
            atrHolder.addValue(IndicatorUtil.calculateTrueRange(highs[i],low[i], c))
        }


        then:
        (ATR.length).times{ n->
            atrHolder.addValue(IndicatorUtil.calculateTrueRange(highs[k+n], low[k+n], close[k+n-1]))
//            System.out.println(atrHolder.values.toString())
            double c=-1

            if(n>0)c= ATR[n-1]

//            System.out.println(String.format("ATR~ N: %d\tc: %f",n, c))
            assert  DoubleMath.fuzzyEquals(IndicatorUtil.calculateATR(atrHolder, c), ATR[n], 0.01)

        }



    }



    def "keep calculating Average true range"(){

    }



}
