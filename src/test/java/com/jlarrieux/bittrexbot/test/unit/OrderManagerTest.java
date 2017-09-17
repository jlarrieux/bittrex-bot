package com.jlarrieux.bittrexbot.test.unit;



import com.google.common.math.DoubleMath;
import com.jlarrieux.bittrexbot.Entity.Market;
import com.jlarrieux.bittrexbot.Entity.Position;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.OrderManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderManagerTest {


    @Autowired
    OrderManager orderManager;



    @Test
    public void testProfitAndLoss() {
        String currency = "eth";
        double quantity = 5;
        double unitPrice = 285;
        Market market = new Market();
        market.setMarketCurrency(currency);
        market.setLast(400);
        Position p = new Position(quantity,unitPrice, currency);
        orderManager.getPositionManager().add(p);

        System.out.println(orderManager.getPandL(market));
        Assert.assertTrue(DoubleMath.fuzzyEquals(575, orderManager.getPandL(market),0.0001));


    }

}
