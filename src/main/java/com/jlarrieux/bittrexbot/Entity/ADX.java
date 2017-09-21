package com.jlarrieux.bittrexbot.Entity;



import com.jlarrieux.bittrexbot.Util.Constants;
import com.jlarrieux.bittrexbot.Util.IndicatorUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Arrays;
import java.util.stream.Collectors;



@Log
@Data
@ToString(exclude = {"trueRangeQueue","DMplusQueue", "DMminusQueue","DXqueue"})
public class ADX {

    double previousTrPeriod =-1, previousDMplusPeriod=-1, previousDMminusPeriod=-1, previousADX=-1, highCurrentMinusPrevious, lowPreviousMinusCurrent;
    double DMplus,  DMminus, DIplus, DIminus, DIdifference, DIsum, DX,currentADX, currentTrueRange, previousPrice, previousHigh, previousLow, trPeriod,DMplusPeriod, DMminusPeriod;
    String marketName;
    DescriptiveStatistics trueRangeQueue = new DescriptiveStatistics(), DMplusQueue= new DescriptiveStatistics(), DMminusQueue= new DescriptiveStatistics(), DXqueue= new DescriptiveStatistics();
    int period, firstLayerPeriod;
    long counter=-1;

    private ADX(){

    }

    public ADX(int period){
        setWindowSize(period);
    }


    public void setWindowSize(int period){
        this.period = period;
        firstLayerPeriod = period/2;
        trueRangeQueue.setWindowSize((firstLayerPeriod));
        DMplusQueue.setWindowSize(firstLayerPeriod);
        DMminusQueue.setWindowSize(firstLayerPeriod);
        DXqueue.setWindowSize(firstLayerPeriod);


    }


    public void update(double high, double low, double currentPrice){
        counter++;
//        log.info(String.format("UPDATING: hig: %f\t low: %f\t price: %f\t", high, low, currentPrice));
        if(counter>0) {
            currentTrueRange = IndicatorUtil.calculateTrueRange(high,low,previousPrice);
            calculateDMs(high,low);
            trueRangeQueue.addValue(currentTrueRange);
        }
        if(counter>=firstLayerPeriod)     calculateFirstLayerQueue();
        if(counter>=period-1) calculateADX();

        previousPrice = currentPrice;
        previousLow = low;
        previousHigh = high;
    }


    private void calculateDMs(double currentHigh, double currentLow){
        DMplus =0;
        DMminus = 0;
        highCurrentMinusPrevious = currentHigh -previousHigh;
        lowPreviousMinusCurrent = previousLow- currentLow;
        if( highCurrentMinusPrevious>0 && highCurrentMinusPrevious> lowPreviousMinusCurrent) DMplus = highCurrentMinusPrevious;
        if(lowPreviousMinusCurrent>0 && lowPreviousMinusCurrent>highCurrentMinusPrevious)DMminus = lowPreviousMinusCurrent;

        //log.info(String.format("Calculating DMs: DMplus = %f\tDMminus = %f\nPrevious high = %f\tPrevious Low = %f\nCurrent high = %f\tCurrent low = %f\n\n",
           //     DMplus, DMminus, previousHigh, previousLow, currentHigh, currentLow));
        DMplusQueue.addValue(DMplus);
        DMminusQueue.addValue(DMminus);

    }


    private void calculateFirstLayerQueue(){
        trPeriod = updateFirstLayerItem(previousTrPeriod, trueRangeQueue);
        DMminusPeriod = updateFirstLayerItem(previousDMminusPeriod, DMminusQueue);
        DMplusPeriod = updateFirstLayerItem(previousDMplusPeriod, DMplusQueue);

//        log.info(String.format("~~~ffINIS tr period: %f\t current previousTR: %f\t counter: %d\n\n",trPeriod, previousTrPeriod, counter ));
        previousTrPeriod = trPeriod;
        previousDMminusPeriod = DMminusPeriod;
        previousDMplusPeriod = DMplusPeriod;
        DIplus = 100*DMplusPeriod/trPeriod;
        DIminus = 100* DMminusPeriod/trPeriod;
        DIdifference = Math.abs(DIplus-DIminus);
        DIsum = DIplus+ DIminus;
        DX = 100*DIdifference/DIsum;

//        log.info(String.format("DX: %.18f\tDMplus: %.18f\tDMminus: %.18f\tcounter: %d\tmarket: %s\n\n", DX, DMplus, DMminus, counter, marketName));
           // log.info(String.format("\nDX = %f\t with DIdifference = %f\tDIsum = %f\tDIplus = %f\tDIminus = %f\tTrPeriod = %f\tPrevioustrPeriod = %f\tMarket: %s\n\n",
              //      DX,DIdifference,DIsum,DIplus, DIminus, trPeriod, previousTrPeriod, marketName));

        DXqueue.addValue(DX);
    }




    private double updateFirstLayerItem(double previous,  DescriptiveStatistics holder){
//        log.info(String.format("holder q to string: %s", Constants.getListAsString(Arrays.stream(holder.getValues()).boxed().collect(Collectors.toList()))));
        double result;
        if(previous==-1 || Double.isNaN(previous)){
            result = holder.getSum();
            previous = result;
            return result;
        }
        else{
            double currentIntegral = holder.getElement((int) (holder.getN()-1));
            result =previous - (previous/firstLayerPeriod)+ currentIntegral;
            previous = result;
            return result;
        }
    }




    private void calculateADX(){
        currentADX = IndicatorUtil.calculateATR(DXqueue, previousADX);
//        if(counter>period)
//        log.info(String.format("~~~(%s)calculating adx: %f\t previousAdx: %f\t counter: %d\nDIdiff: %f\t DIsum: %f\tDIminus: %f\t DIplus: %f\nDMplusPeriod: %f\t DMminusPeriod: %f\ttrPeriod: %f\tpreviousTrPeriod: %f\nDmMinus: %f\t DMplus: %f\nWith dx q: %s\n\n"
//                ,marketName, currentADX, previousADX, counter, DIdifference,DIsum,DIminus,DIplus,DMplusPeriod,DMminusPeriod,trPeriod, previousTrPeriod, DMminus, DMplus,     Constants.getListAsString(Arrays.stream(DXqueue.getValues()).boxed().collect(Collectors.toList()))));
        previousADX = currentADX;

    }



    public String getDXqueueAsString(){
        return Constants.getListAsString(Arrays.stream(DXqueue.getValues()).boxed().collect(Collectors.toList()));
    }


    public double getADXDirection(){
        return DIplus-DIminus;
    }


    public String getDMcomponents(){
        return String.format("DMplus: %f\t\t&emsp;&emsp;DMminus: %f\n\n<br><br>highCurrentMinusPrevious: %f\t\t&emsp;&emsp;lowPreviousMinusCurrent: %f"
                ,DMplus, DMminus, highCurrentMinusPrevious, lowPreviousMinusCurrent);
    }

}
