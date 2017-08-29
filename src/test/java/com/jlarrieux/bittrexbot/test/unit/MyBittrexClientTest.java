package com.jlarrieux.bittrexbot.test.unit;



import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBittrexClientTest {


    @Autowired
    MyBittrexClient client;

    Response response;



    @Test
    public void testGetMarkets() {
        response = client.getMarkets();
        assertTrue(response.isSuccess());
    }



    @Test
    public void testGetMarketSummaries() {
        response = client.getMarketSummaries();
        assertTrue(response.isSuccess());

    }



    @Test
    public void testGoodMarketSummary() {
        response = client.getMarketSummary("BTC-ETH");
        assertTrue(response.isSuccess());

    }



    @Test
    public void testBadMarketSummary() {
        response = client.getMarketSummary("BTC-BTC");
        assertFalse(response.isSuccess());

    }



    @Test
    public void testGetOpenOrders() {
        response = client.getOpenOrders();
        assertTrue(response.isSuccess());

    }



    @Test
    public void testGetBalances() {
       response = client.getBalances();
       assertTrue(response.isSuccess());

    }



    @Test
    public void testGetBalance() {
        response = client.getBalance("BTC");
        assertTrue(response.isSuccess());

    }



    @Test
    public void testGetOrderHistory() {
        response = client.getOrderHistory("BTC-ETH");
        assertTrue(response.isSuccess());

    }












}
