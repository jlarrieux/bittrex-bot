package com.jlarrieux.bittrexbot.Manager;



import com.google.gson.JsonArray;
import com.jlarrieux.bittrexbot.Entity.Order;
import com.jlarrieux.bittrexbot.Entity.Orders;
import com.jlarrieux.bittrexbot.REST.BittrexRestClient;
import com.jlarrieux.bittrexbot.REST.Response;
import com.jlarrieux.bittrexbot.Util.JsonParserUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Log
@Component
public class OrderManager {


    private Orders orderBooks;
    private BittrexRestClient client;





    @Autowired
    public OrderManager(BittrexRestClient client, Orders orders){
        this.client = client;
        this.orderBooks = orders;

       getOpenOrders();
    }




    private void getOpenOrders(){
        Response response = client.getOpenOrders();
        log.info("doin orders");
        if(JsonParserUtil.isAsuccess(response)) {
            iterateJsonArrayOrder(JsonParserUtil.getJsonArrayFromJsonString(response.getResult()));
        }
    }


    private void iterateJsonArrayOrder(JsonArray array){
        Order order;
        for(int i =0; i<array.size(); i++){
            order = orderBooks.add(array,i);
            log.info(String.format("Order book: %s", order.toString()));
        }
    }
}
