package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;


import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
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

        log.info(GET_MARKET_SUMMARIES_LOG_DIVIDER);
        log.info("Inside: " + getClass().getSimpleName() +"\t Method: " + "getMarketSummaries()");
        marketManager.addMarkets(marketSummaryAdapter.getMarketSummaries("BTC-CLUB"));

        long totalTime = System.nanoTime() - startTime;



        numberOfExecution++;
        if (numberOfExecution == 1) {
            lowestExecutionTimeRecorded = totalTime;
        }
        totalTimeOfExecution = totalTime + totalTimeOfExecution;
        highestExecutiontimeRecorded = highestExecutiontimeRecorded < totalTime ? totalTime:highestExecutiontimeRecorded;
        lowestExecutionTimeRecorded = lowestExecutionTimeRecorded > totalTime ? totalTime:lowestExecutionTimeRecorded;

        log.info("Time elapsed for Current Execution BittrexDatamanger: " +
                String.format("%dms",TimeUnit.NANOSECONDS.toMillis(totalTime)));
        log.info("Number of Execution: " + numberOfExecution);
        log.info("Highest Execution time: " + TimeUnit.NANOSECONDS.toMillis(highestExecutiontimeRecorded));
        log.info("Shortest Execution time: " + TimeUnit.NANOSECONDS.toMillis(lowestExecutionTimeRecorded));
        log.info("Average Execution time: " + TimeUnit.NANOSECONDS.toMillis(totalTimeOfExecution/numberOfExecution));

    }






}
