package com.jlarrieux.bittrexbot.Util;

import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.BittrexDataManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.PortFolio;
import com.jlarrieux.bittrexbot.simulation.DAO.IDBExchangeDAO;
import com.jlarrieux.bittrexbot.simulation.TO.AnalyticsTransaction;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

import static com.jlarrieux.bittrexbot.Util.Constants.GET_MARKET_SUMMARIES_LOG_DIVIDER;

@Data
@Log4j2
@Component
public class Analytics {


    private PortFolio portFolio;
    private PortFolio previousPortFolio;

    private int invocationCounter;


    private SimulationProperties properties;

    private double currentPortFolioValue, previousPortFolioValue;
    private double currentPandL, previousPandL;
    private double currentBTCBalance, previousBTCBalance;

    //format: 2017-08-15 07:54:15.0
    private String dateInProcess;

    private long timeProcessStarted;

    private static Analytics analytics;

    private BittrexDataManager bittrexDataManager;

    @Autowired
    @Qualifier("DBExchangeDAOImpl")
    private IDBExchangeDAO dbExchangeDAO;

    public enum OrderType{
        BUY,SELL
    }

    @Autowired
    private Analytics(SimulationProperties simulationProperties, BittrexDataManager bittrexDataManager) {
        this.bittrexDataManager = bittrexDataManager;
        this.portFolio = bittrexDataManager.getPortFolio();
        properties = simulationProperties;
        log.info(createStartOfLogOuput());
    }

    public void setProcessingDate(String dateStr) {
        this.dateInProcess = dateStr;
    }

    public String getProcessingDate() {
        return dateInProcess;
    }

    public void addTransaction(OrderType orderType,
                               Market market,
                               double quantity,
                               double unitPrice) {

        portFolio = bittrexDataManager.getPortFolio();
        double total = unitPrice * quantity;
        if (invocationCounter > 0) {
            previousPortFolioValue = currentPortFolioValue;
        }
        currentPortFolioValue = portFolio.getCurrentPortFolioValue();
        double fluctuationPercentage = calculateFluctuationPercentage(previousPortFolioValue, currentPortFolioValue);
        String transactionType = orderType == OrderType.BUY ? "BUY" : "SELL";
        invocationCounter++;
        log.info(GET_MARKET_SUMMARIES_LOG_DIVIDER);
        log.info("Transaction Number: " + invocationCounter
                + " Date: " + getDateInProcess());
        log.info("Transaction Type: " + transactionType);
        log.info("MarketName: " + market.getMarketName()
                + ". Quantity: " + quantity
                + ". UnitPrice: " + unitPrice
                + ". Total: " + unitPrice * quantity);
        log.info("ADX Value: " + market.getAdxValue());
        log.info("Bollinger H: " + market.getBollingerHigh()
                + ". Bollinger M: " + market.getBollingerMid()
                + ". Bollinger L: " + market.getBollingerLow());
        log.info("Current Value: \t" + currentPortFolioValue);
        log.info("P and L: \t" + portFolio.profitAndLossPercentage());
        log.info("BTC Current Amount: \t" + portFolio.getBTCBalance());
        log.info("Fluctuation Percentage from Last Transaction: "+fluctuationPercentage);
        log.info("Positions ------>\n" + getPositionsString().toString());
        log.info("RSI: " + market.getCurrentRSI());
        log.info("KELTNER CHANNELS: \n" +market.getKeltnerChannels().toString() + "\n");

        if (properties.isTrxnDbLog()) {
            AnalyticsTransaction analyticsTransaction =
                    AnalyticsTransaction.createAnalyticsTransaction(
                            orderType,
                            dateInProcess,
                            timeProcessStarted,
                            System.currentTimeMillis(),
                            market.getMarketName(),
                            getPositionsString().toString(),
                            market.getKeltnerChannels().toString(),
                            invocationCounter,
                            quantity,
                            unitPrice,
                            unitPrice*quantity,
                            market.getAdxValue(),
                            market.getBollingerHigh(),
                            market.getBollingerMid(),
                            market.getBollingerLow(),
                            currentPortFolioValue,
                            previousPortFolioValue,
                            portFolio.profitAndLossPercentage(),
                            portFolio.getBTCBalance(),
                            fluctuationPercentage,
                            market.getCurrentRSI()
                    );
            logTransactionInDb(analyticsTransaction);
        }
    }

    private StringBuilder getPositionsString() {
        StringBuilder b = new StringBuilder();
        b.append("\n");
        Positions positions = portFolio.getPositionManager().getPositionBooks();
        Set<String> list = positions.keySet();
        for(String s: list){
            b.append(positions.get(s).toString() + "\n");
        }
        return b;
    }

    private double calculateFluctuationPercentage(double previousPortFolioValue, double currentPortFolioValue){

        double value;
        if (previousPortFolioValue == 0) {
            value = 0.0;
        } else {
            value  = ((currentPortFolioValue - previousPortFolioValue)/previousPortFolioValue) * 100;
        }

        return value;
    }

    private String createStartOfLogOuput(){
        StringBuilder strBuiler = new StringBuilder();
        strBuiler.append("START OF ANALYTICS LOG\n*************************************************\n");
        Date date = new Date();
        timeProcessStarted = date.getTime();
        strBuiler.append(date);
        strBuiler.append("\t\t\t\t\t*");
        strBuiler.append("\n*************************************************\n");
        return strBuiler.toString();
    }

    private void logTransactionInDb(AnalyticsTransaction analyticsTransaction){
        dbExchangeDAO.logAnalyticsTransaction(analyticsTransaction);
    }
}
