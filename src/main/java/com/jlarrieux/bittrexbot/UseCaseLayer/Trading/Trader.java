package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;



public interface Trader {

    void makeBuy(Market market);
    void executeStrategy();
    void makeSell(Market market);



}
