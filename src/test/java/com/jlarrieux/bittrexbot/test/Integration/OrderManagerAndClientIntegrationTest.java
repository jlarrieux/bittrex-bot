package com.jlarrieux.bittrexbot.test.Integration;



import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.MarketSummary;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;



@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderManagerAndClientIntegrationTest {


    @Autowired
    static MyBittrexClient client;


    @Autowired
    OrderManager orderManager;

    @Autowired
    MarketSummary marketSummary;

    static StringBuilder builder = new StringBuilder();

    private static boolean initialized = false;
    String marketName ="BTC-HMQ";

    @Autowired
    public void setClient(MyBittrexClient client){
        OrderManagerAndClientIntegrationTest.client = client;
    }





    @Before
    public void setUpOnce(){
        if(!initialized) {

            marketSummary.executeMarketSearch(marketName);
            double underValuedBTCBuyPrice = marketSummary.getLast()/10;

            double quantity =Math.ceil(0.0005/underValuedBTCBuyPrice);
            System.out.println(String.format("last: %f\tundervalued: %f\tquantity: %f", marketSummary.getLast(), underValuedBTCBuyPrice, quantity));

            builder.append(orderManager.initiateBuy(marketName, quantity, underValuedBTCBuyPrice));
            System.out.println("orderid: "+ builder.toString());
            initialized = true;
        }
    }





    @org.junit.Test
    public void testOrder() {
        assertTrue(orderManager.getPendingBuyOrderTracker().size()==1);
    }



    @Test
    public void testOrderHasRightUUID() {
        assertTrue(orderManager.getPendingBuyOrderTracker().containsKey(builder.toString()));
    }



    @Test
    public void testCancelOrder() throws InterruptedException {
        marketSummary.executeMarketSearch(marketName);
        double under = marketSummary.getLast()/10;
        double quantity = Math.ceil(0.0005/under);

        String localUUID= orderManager.initiateBuy(marketName,quantity,under);
        System.out.println("special "+localUUID);
        assertTrue(orderManager.getPendingBuyOrderTracker().containsKey(localUUID));
        Order order = orderManager.getPendingBuyOrderTracker().get(localUUID);

        assertTrue(order.getIsOpen());
        Response r= client.cancelOrder(order.getOrderUuid());
        if(JsonParserUtil.isAsuccess(r)) {
            Thread.sleep(1500);
            Response r1 = client.getOrder(order.getOrderUuid());
            if(JsonParserUtil.isAsuccess(r1)) {
                System.out.println(r1.toString());
                Order o = new Order(JsonParserUtil.getJsonObjectFromJsonString(r1.getResult()));
                System.out.println(o.toString());
                assertTrue(!o.getIsOpen());
                assertTrue(o.getCancelIniated());
            }
        }

    }


    @AfterClass
    public static void tearDOwnAll(){
        System.out.println("~~~~~TEARDOWN: UUID: "+ builder.toString());
        client.cancelOrder(builder.toString());
    }





}
