package com.jlarrieux.bittrexbot.test.unit;



import com.jlarrieux.bittrexbot.UseCaseLayer.Adapter.MarketOrderBookAdapater;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketOrderBookAdapterTest {


    @Autowired
    MarketOrderBookAdapater marketOrderBookAdapater;



    @Test
    public void testOrderBooksAreEmpty() {

        assertTrue(marketOrderBookAdapater.getBuyOrderBookEntries().size()==0);
        assertTrue(marketOrderBookAdapater.getSellOrderBookEntries().size()==0);

    }



    @Test
    public void testOrderBooksAreFilled() {
        marketOrderBookAdapater.executeMarketOrderBook("BTC-ETH");
        assertTrue(marketOrderBookAdapater.getBuyOrderBookEntries().size()>0);
        assertTrue(marketOrderBookAdapater.getSellOrderBookEntries().size()>0);
    }



}
