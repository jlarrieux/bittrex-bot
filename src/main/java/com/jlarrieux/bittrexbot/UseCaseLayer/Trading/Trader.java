package com.jlarrieux.bittrexbot.UseCaseLayer.Trading;



import com.jlarrieux.bittrexbot.Entity.Market;



public interface Trader {

    void evaluateBuy(Market market);
    void executeStrategy();
    void evaluateSell(Market market);



}
