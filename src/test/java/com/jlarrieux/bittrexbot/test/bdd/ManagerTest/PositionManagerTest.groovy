package com.jlarrieux.bittrexbot.test.bdd.ManagerTest

import com.google.common.math.DoubleMath
import com.jlarrieux.bittrexbot.Entity.Position
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.PositionManager
import com.jlarrieux.bittrexbot.Util.Constants
import spock.lang.Specification

class PositionManagerTest extends Specification{


    def"Addition positions to positionmanager"(){
        given:"A position manager with a position added"
        String marketCurrent = "jeannius"
        PositionManager positionManager = new PositionManager();
        Position p = new Position(10,5,marketCurrent)
        positionManager.add(p)


        when:"anoter position is added"
        Position p1 = new Position(5,10, marketCurrent)
        positionManager.add(p1)
        Position p2 = positionManager.getPositionBooks().get(marketCurrent)
        System.out.println(positionManager.getPositionBooks().get(marketCurrent))


        then:
        assert positionManager.getPositionBooks().size()==1
        assert p2.getCurrency().equals(marketCurrent)
        assert DoubleMath.fuzzyEquals(p2.getQuantity(),15, Constants.DECIMAL_PRECISION_EQUAL)
        assert DoubleMath.fuzzyEquals(p2.getAveragePurchasedPrice(), 6.666, Constants.DECIMAL_PRECISION_EQUAL)

    }
}
