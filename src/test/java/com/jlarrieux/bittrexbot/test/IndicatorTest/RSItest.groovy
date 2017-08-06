package com.jlarrieux.bittrexbot.test.IndicatorTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Util.IndicatorUtil
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import spock.lang.Specification

class RSItest extends Specification{

    def "RSI from http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:relative_strength_index_rsi"(){
        given: "A gainqueue and a loss queue"
        DescriptiveStatistics gains = new DescriptiveStatistics();
        DescriptiveStatistics loss = new DescriptiveStatistics();

        when:"Populated with the following data"
        double[] g = [0.06,0.72,0.5,0.27,0.33,0.42,0.24,0.14,0.67]
        double[] l =[0.25,0.54,0.19,0.42,]
        for(int i=0; i<g.length;i++)gains.addValue(g[i])
        for(int i=0; i<l.length;i++)loss.addValue(l[i])


        then: "The RSI should be 70.53"
//        assert IndicatorUtil.calculateRSI(gains, loss) == 70.53
        assert DoubleMath.fuzzyEquals(IndicatorUtil.calculateRSI(gains,loss), 70.53, 0.05)

    }



}
