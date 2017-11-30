package com.jlarrieux.bittrexbot.simulation.TO;

import com.jlarrieux.bittrexbot.Util.Analytics;
import lombok.Data;

@Data
public class AnalyticsTransaction {

    private String marketName;
    private String keltnerChannels;
    private String positions;
    private String dateInProcess;
    private long timeProcessStarted;
    private long timeTransactionIsProcessed;
    private Analytics.OrderType transactionType;
    private int invocationCounter;
    private double quantity, unitPrice, transactionTotal;
    private double adxValue;
    private double bollingerHigh, bollingerMid, bollingerLow;
    private double previousPortFolioValue, currentPortFolioValue;
    private double profitNLossPercentage;
    private double btcBalance;
    private double fluctuationPercentage;
    private double rsiValue;


    public AnalyticsTransaction(){}

    public static AnalyticsTransaction createAnalyticsTransaction(
            Analytics.OrderType transactionType,
            String dateInProcess,
            long timeProcessStarted,
            long timeTransactionIsProcessed,
            String marketName,
            String positions,
            String keltnerChannels,
            int invocationCounter,
            double quantity,
            double unitPrice,
            double transactionTotal,
            double adxValue,
            double bollignerHigh,
            double bollignerMid,
            double bollignerLow,
            double currentPortFolioValue,
            double previousPortFolioValue,
            double profitNLossPercentage,
            double btcBalance,
            double fluctuationPercentage,
            double rsiValue) {

        AnalyticsTransaction analyticsTrns = new AnalyticsTransaction();

        analyticsTrns.setTransactionType(transactionType);
        analyticsTrns.setDateInProcess(dateInProcess);
        analyticsTrns.setTimeProcessStarted(timeProcessStarted);
        analyticsTrns.setTimeTransactionIsProcessed(timeTransactionIsProcessed);
        analyticsTrns.setMarketName(marketName);
        analyticsTrns.setPositions(positions);
        analyticsTrns.setKeltnerChannels(keltnerChannels);
        analyticsTrns.setInvocationCounter(invocationCounter);
        analyticsTrns.setQuantity(quantity);
        analyticsTrns.setUnitPrice(unitPrice);
        analyticsTrns.setTransactionTotal(transactionTotal);
        analyticsTrns.setAdxValue(adxValue);
        analyticsTrns.setBollingerHigh(bollignerHigh);
        analyticsTrns.setBollingerMid(bollignerMid);
        analyticsTrns.setBollingerLow(bollignerLow);
        analyticsTrns.setCurrentPortFolioValue(currentPortFolioValue);
        analyticsTrns.setPreviousPortFolioValue(previousPortFolioValue);
        analyticsTrns.setBtcBalance(btcBalance);
        analyticsTrns.setFluctuationPercentage(fluctuationPercentage);
        analyticsTrns.setProfitNLossPercentage(profitNLossPercentage);
        analyticsTrns.setRsiValue(rsiValue);

        return analyticsTrns;
    }

}
