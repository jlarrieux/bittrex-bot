package com.jlarrieux.bittrexbot.Manager;



import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.REST.BittrexRestClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class BittrexDataManager {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BittrexDataManager.class);
    @Autowired
    private BittrexRestClient client;
    private Response response;


    @Autowired
    private Markets books;

    @Autowired
    OrderManager manager;


    public BittrexDataManager(){

    }


    @Scheduled(fixedRate = 10000)
    public void getMarketSummaries(){
        response = client.getMarketSummaries();
        if(JsonParserUtil.isAsuccess(response)){
//            iterateJsonArrayMarketSummary(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
        }

    }



    private void iterateJsonArrayMarketSummary(JsonArray array){
        Market market;
        for(int i=0; i<array.size(); i++){
//            if(i>5) break;
            market=books.add(array, i);
            if(market!=null)   log.info(String.format("%d- %s size of queue: %d %n %s%n%S%n%n",i,market.getMarketName()+"\t",market.getPriceQueue().getN(), market.bollingerToString(), market.RSItoString()));
        }
    }




}
