package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;


import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import com.jlarrieux.bittrexbot.UseCaseLayer.PortFolio;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;



@Log4j2
@Component
@Profile("!test")
public class BittrexDataManager {


    @Autowired
    private MarketSummaryAdapter marketSummaryAdapter;

    @Autowired
    private MarketManager marketManager;

    @Autowired
    private PortFolio portFolio;

    private final String GET_MARKET_SUMMARIES_LOG_DIVIDER = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
            "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

    private static long numberOfExecution;
    private static long totalTimeOfExecution;
    private static long lowestExecutionTimeRecorded;
    private static long highestExecutiontimeRecorded;




    public BittrexDataManager(){

    }


    @Scheduled(fixedDelay = 1)
    public void getMarketSummaries(){

        long startTime = System.nanoTime();

        log.debug(GET_MARKET_SUMMARIES_LOG_DIVIDER);
        //log.debug("Inside: " + getClass().getSimpleName() +"\t Method: " + "getMarketSummaries()");

        marketManager.addMarkets(marketSummaryAdapter.getMarketSummaries());

        long totalTime = System.nanoTime() - startTime;

        numberOfExecution++;

        if (numberOfExecution == 1) {
            lowestExecutionTimeRecorded = totalTime;
        }

        totalTimeOfExecution = totalTime + totalTimeOfExecution;

        highestExecutiontimeRecorded = highestExecutiontimeRecorded < totalTime ? totalTime:highestExecutiontimeRecorded;

        lowestExecutionTimeRecorded = lowestExecutionTimeRecorded > totalTime ? totalTime:lowestExecutionTimeRecorded;

        log.debug("Time elapsed for Current Execution BittrexDatamanger: " +
                String.format("%dms",TimeUnit.NANOSECONDS.toMillis(totalTime)));
        log.debug("Number of Execution: " + numberOfExecution);
        log.debug("Highest Execution time: " + TimeUnit.NANOSECONDS.toMillis(highestExecutiontimeRecorded));
        log.debug("Shortest Execution time: " + TimeUnit.NANOSECONDS.toMillis(lowestExecutionTimeRecorded));
        log.debug("Average Execution time: " + TimeUnit.NANOSECONDS.toMillis(totalTimeOfExecution/numberOfExecution));
        log.debug(
        String.format("Current Value: %f<br><br>P and L: %f %%<br><br>BTC current amount: %f"
                , portFolio.getCurrentPortFolioValue(), portFolio.profitAndLossPercentage(), portFolio.getBTCBalance()));
    }






}
