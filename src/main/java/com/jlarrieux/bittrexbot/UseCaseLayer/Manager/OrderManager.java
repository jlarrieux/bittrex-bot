package com.jlarrieux.bittrexbot.UseCaseLayer.Manager;



import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.Entity.Orders;
import com.jlarrieux.bittrexbot.REST.MyBittrexClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;



@Log
@Component
public class OrderManager {


    private Orders orderBooks;
    private MyBittrexClient client;





    @Autowired
    public OrderManager(MyBittrexClient client, Orders orders){
        this.client = client;
        this.orderBooks = orders;
       getOpenOrders();
    }




    public void getOpenOrders(){
        log.info("Getting open orders...");
        Response response = new Response(client.getOpenOrders());
        if(response.isSuccess())  iterateJsonArrayOrder(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));

    }


    private void iterateJsonArrayOrder(JsonArray array){
        orderBooks.clear();
        Order order;
        for(int i =0; i<array.size(); i++) orderBooks.add(array,i);

    }



    public void cancelAll() {
        getOpenOrders();
        log.info("Orderbook size: "+ orderBooks.size());
        for(String key: orderBooks.keySet()){
            Order o = orderBooks.get(key);
            log.info(String.format("Cancelling order: %s", o.toString()));
            log.info( client.cancelOrder(o.getOrderUuid()));

        }
    }


    public void cancelOldOrders(LocalDateTime cutoff){
        getOpenOrders();
//        log.info("orderbook size:"+String.valueOf(orderBooks.size()));
        for(String key: orderBooks.keySet()){
            Order o = orderBooks.get(key);
            boolean before = o.getOpened().isBefore(cutoff);
//            log.info(String.format("is it before? %b\t order date: %s \t\t cutoffdate: %s", before, o.getOpened().toString(), cutoff.toString()));
            if(before) client.cancelOrder(o.getOrderUuid());
        }
    }



}
