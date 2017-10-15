package com.jlarrieux.bittrexbot.simulation.DAO;

import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.jlarrieux.bittrexbot.simulation.TO.BuyTO;
import com.jlarrieux.bittrexbot.simulation.TO.OrderTO;
import com.jlarrieux.bittrexbot.simulation.TO.SellTO;
import com.jlarrieux.bittrexbot.simulation.db.DBConnectionPool;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashMap;

@Log
@Component("inMemoryDbDAO")
public class InMemoryDbDAO extends DBExchangeDAOImpl{

    private HashMap<String, OrderTO> orderHashMap;

    @Autowired
    public InMemoryDbDAO(DBConnectionPool dbConnectionPool, SimulationProperties simulationProperties){
        //TODO nasty hack. Needs to be changed. This is used insted of super to avoid dataSrouce being created twice
        populateInstances(dbConnectionPool, simulationProperties);
        orderHashMap = new HashMap<>();
    }

    @Override
    public BuyTO buy(String uuid, String marketName, double quantity, double price) {

        addOrder(createOrderTO(uuid, marketName, quantity, price));

        BuyTO buyTO = new BuyTO();
        BuyTO.Result result = buyTO.createResult();
        result.setUuid(uuid);
        buyTO.setResult(result);

        log.info("HashMap Size: " + orderHashMap.size());

        return buyTO;
    }

    @Override
    public SellTO sell(String uuid, String marketName, double quantity, double price) {

        updateAvailalbeBalance(quantity, price);
        popOrder(uuid);

        SellTO sellTO = new SellTO();
        SellTO.Result result = sellTO.createResult();
        result.setUuid(uuid);
        sellTO.setResult(result);

        return sellTO;
    }

    private OrderTO createOrderTO(String uuid, String marketName, double quantity, double price) {
        OrderTO orderTO = new OrderTO();
        OrderTO.Result result = orderTO.createResult();
        orderTO.setResult(result);

        result.setOrderUuid(uuid);
        result.setQuantity(quantity);
        result.setPrice(price);
        result.setExchange(marketName);

        return orderTO;
    }

    private void addOrder(OrderTO orderTO) {
        orderHashMap.put(orderTO.getResult().getOrderUuid(), orderTO);
    }

    @Nullable
    @Override
    public OrderTO getOrder(String uuid) {
        return orderHashMap.get(uuid.toString());
    }

    @Nullable
    private OrderTO popOrder(String uuid) {
        OrderTO orderTO = getOrder(uuid.toString());
        if (orderTO != null) {
            orderHashMap.remove(uuid.toString());
        }
        return orderTO;
    }
}
