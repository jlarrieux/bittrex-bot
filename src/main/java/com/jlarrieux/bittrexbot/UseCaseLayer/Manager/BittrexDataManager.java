package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
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


    @Scheduled(fixedRate = 30000)
    public void getMarketSummaries(){
        Response response = new Response("yeah");// marketSummaryAdapter.getMarketSummaries();
        //client2.cancelOrder("");
//        Response r = client2.placeOrder("","btc-vtr","","1","0.00004218","1","","");
        if(response.isSuccess())
            marketManager.addMarkets(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
    }
}
