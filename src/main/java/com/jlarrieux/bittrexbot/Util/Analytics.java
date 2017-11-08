package com.jlarrieux.bittrexbot.Util;

import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.PortFolio;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Analytics analytics;

    public enum OrderType{
        BUY,SELL
    }

    @Autowired
    private Analytics(PortFolio portFolio, SimulationProperties simulationProperties,
                      OrderManager orderManager) {
        this.portFolio = portFolio;
        properties = simulationProperties;
        log.info(createStartOfLogOuput());
    }

    public void addTransaction(OrderType orderType,
                               String marketName,
                               double quantity,
                               double unitPrice) {

        double total = unitPrice * quantity;
        if (invocationCounter > 0) {
            previousPortFolioValue = currentPortFolioValue;
        }
        currentPortFolioValue = portFolio.getCurrentPortFolioValue();
        double fluctuationPercentage = calculateFluctuationPercentage(previousPortFolioValue, currentPortFolioValue);
        String transactionType = orderType == OrderType.BUY ? "BUY" : "SELL";
        invocationCounter++;
        log.info(GET_MARKET_SUMMARIES_LOG_DIVIDER);
        log.info("Transaction Number: " + invocationCounter);
        log.info("Transaction Type: " + transactionType);
        log.info("MarketName: " + marketName
                + ". Quantity: " + quantity
                + ". UnitPrice: " + unitPrice
                + ". Total: " + unitPrice * quantity);
        log.info("Current Value: \t" + currentPortFolioValue);
        log.info("P and L: \t" + portFolio.profitAndLossPercentage());
        log.info("BTC Current Amount: \t" + portFolio.getBTCBalance());
        log.info("Fluctuation Percentage from Last Transaction: "+fluctuationPercentage);


        StringBuilder b = new StringBuilder();
        b.append("\n");
        Positions positions = portFolio.getPositionManager().getPositionBooks();
        Set<String> list = positions.keySet();
        for(String s: list){
            b.append(positions.get(s).toString()+"\n");
        }
        log.info(b.toString());


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
        strBuiler.append(date.toString());
        strBuiler.append("\t\t\t\t\t*");
        strBuiler.append("\n*************************************************\n");
        return strBuiler.toString();
    }



}
