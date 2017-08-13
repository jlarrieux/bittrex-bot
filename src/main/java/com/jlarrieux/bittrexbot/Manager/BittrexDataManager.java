package com.jlarrieux.bittrexbot.Manager;



import com.jlarrieux.bittrexbot.REST.BittrexRestClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log
@Component
public class BittrexDataManager {


    @Autowired
    private BittrexRestClient client;

    @Autowired
    private MarketManager marketManager;

    @Autowired
    OrderManager manager;


    public BittrexDataManager(){

    }


    @Scheduled(fixedRate = 10000)
    public void getMarketSummaries(){
        Response response = client.getMarketSummaries();
        if(JsonParserUtil.isAsuccess(response)){
            marketManager.addMarkets(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
        }

    }






}
