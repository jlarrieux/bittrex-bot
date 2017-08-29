package com.jlarrieux.bittrexbot.test.bdd.EntityTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.Position
import spock.lang.Specification

class PositionTest extends Specification {


    def "Calculate correct Price for different insertion"(){
        given: "A position with a priceQueue"
        Position position= new Position(2, 5, "test");


        when: "Position is increased with another entry"
        position.update(3,6);

        then:"Total, quantity, and priceQueue should be correctly calculated"
        assert position.total==28;
        assert position.quantity==5;
        assert DoubleMath.fuzzyEquals(position.getAveragePurchasedPrice(),5.6, 0.00000001);


    }



    def"Report accurate average price for 1 entry"(){
        given: "A position"
        Position position = new Position(2, 5, "test")


        expect:
        assert position.getAveragePurchasedPrice()==5
    }




















}
