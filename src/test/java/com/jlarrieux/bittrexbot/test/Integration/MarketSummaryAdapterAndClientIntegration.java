package com.jlarrieux.bittrexbot.test.Integration;



import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketSummaryAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketSummaryAdapterAndClientIntegration {


    @Autowired
    MarketSummaryAdapter marketSummaryAdapter;





    @Test
    public void testMarketCurrency() {
        String marketName = "BTC-ETH";
        String marketCurrency = "ETH";
        System.out.println(marketSummaryAdapter.getMarketCurrencyShort(marketName));
        Assert.assertTrue(marketCurrency.equals(marketSummaryAdapter.getMarketCurrencyShort(marketName)));
    }



}
