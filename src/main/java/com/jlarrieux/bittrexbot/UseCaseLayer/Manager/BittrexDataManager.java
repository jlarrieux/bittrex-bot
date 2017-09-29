package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log
@Component
@Profile("!test")
public class BittrexDataManager {


    @Autowired
    private MarketSummaryAdapter marketSummaryAdapter;

    @Autowired
    private MarketManager marketManager;




    public BittrexDataManager(){

    }


    @Scheduled(fixedRate = 100)
    public void getMarketSummaries(){
        log.info("Inside: " + getClass().getSimpleName() +"\t Method: " + "getMarketSummaries()");
        marketManager.addMarkets(marketSummaryAdapter.getMarketSummaries());
    }






}
