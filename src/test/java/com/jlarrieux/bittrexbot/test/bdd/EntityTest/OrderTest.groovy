package com.jlarrieux.bittrexbot.test.bdd.EntityTest

import com.jlarrieux.bittrexbot.Entity.Order
import com.jlarrieux.bittrexbot.Util.JsonParserUtil
import spock.lang.Specification

class OrderTest extends Specification {
    
    
    def "Testing order creation"(){
        given:
        String json = '{"AccountId":null,"OrderUuid":"185fea49-4c11-4e10-91b3-19e2762d6b08","Exchange":"BTC-HMQ","Type":"LIMIT_BUY","Quantity":50.00000000,"QuantityRemaining":50.00000000,"Limit":0.00001000,"Reserved":0.00050000,"ReserveRemaining":0.00050000,"CommissionReserved":0.00000125,"CommissionReserveRemaining":0.00000125,"CommissionPaid":0.00000000,"Price":0.00000000,"PricePerUnit":null,"Opened":"2017-08-25T03:07:25.723","Closed":null,"IsOpen":true,"Sentinel":"395ea938-8d02-41d5-b7aa-34991e69c4ac","CancelInitiated":false,"ImmediateOrCancel":false,"IsConditional":false,"Condition":"NONE","ConditionTarget":null}'

        when:
        Order order = new Order(JsonParserUtil.getJsonObjectFromJsonString(json))

        then:
        assert order!= null
        assert  order.getMarketName().equals("BTC-HMQ")
        
    }
}
