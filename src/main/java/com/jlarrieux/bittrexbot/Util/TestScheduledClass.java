package com.jlarrieux.bittrexbot.Util;

import com.jlarrieux.bittrexbot.REST.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduledClass {


    @Scheduled(fixedRate = 1000)
    public void getMarketSummaries(){
        System.out.println("Keep on printing");
    }
}
