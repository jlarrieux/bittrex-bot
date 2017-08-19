package com.jlarrieux.bittrexbot;



import com.google.gson.JsonObject;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Markets;
import com.jlarrieux.bittrexbot.REST.JLarrieuxRestClient;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;

import java.util.concurrent.*;


@Log
public class OtherTestThreads {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(500);
        long start = System.currentTimeMillis();
        Markets marketBooks = new Markets();
        for(int i=0; i<199; i++){
            Future <Market> f = executorService.submit(new bittrexWorker(i+1));
            synchronized (OtherTestThreads.class){
                marketBooks.add(f.get());
            }
        }
        executorService.shutdown();
        log.info("Printing markets");
        for(String key: marketBooks.keySet()){
            Market m = marketBooks.get(key);
            log.info(String.format("Market name: %s\tBaseCurrency: %s\tMarketCurrency: %s\tbid: %f", m.getMarketName(), m.getBaseCurrency(), m.getMarketCurrency(),m.getBid()));
        }
        long finish = System.currentTimeMillis();
        long milli = finish - start;
        double seconds = (double) milli/1000;
        log.info(String.format("It took %d milliseonds (%.2f seconds for all markets", milli, seconds));
    }



















    static class bittrexWorker implements Callable<Market>{
        private int marketId;

        public bittrexWorker(int id){
            this.marketId = id;
        }

        @Override
        public Market call() throws Exception {
            long start = System.currentTimeMillis();
            JLarrieuxRestClient jLarrieuxRestClient = new JLarrieuxRestClient();
            JsonObject object = JsonParserUtil.getJsonObjectFromJsonString(jLarrieuxRestClient.getMarketById(marketId));
            Market market = new Market();
            market.alternateConstruction(object);
            long finish = System.currentTimeMillis();
            long milli = finish - start;
            double seconds = (double) milli/1000;
            log.info(String.format("Thread id: %d took %d milliseonds (%.2f seconds for all markets",marketId, milli, seconds));
            return market;
        }
    }


}
