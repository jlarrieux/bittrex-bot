package com.jlarrieux.bittrexbot.REST.Controller;



import com.jlarrieux.bittrexbot.Entity.Positions;
import com.jlarrieux.bittrexbot.UseCaseLayer.Decider;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.MarketManager;
import com.jlarrieux.bittrexbot.UseCaseLayer.PortFolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;



@RestController
@RequestMapping("/")
public class MyController {

    private Decider decider;
    private PortFolio portFolio;
    private MarketManager markets;

    @Autowired
    public MyController(Decider decider, MarketManager marketManager, PortFolio portFolio){
        this.decider = decider;
        this.markets = marketManager;
        this.portFolio = portFolio;
    }


    @GetMapping("/getcoin")
    public String getCoinInsight(@RequestParam(value = "coin", required = true) String coin){
        return markets.getCoinInsight(coin.toUpperCase());
    }


    @GetMapping("/adx")
    public String getADX(@RequestParam(value = "coin", required = true) String coin){
        return markets.getMarket(coin.toUpperCase()).getAdx().toString();
    }


    @GetMapping("/dx")
    public String getDx(@RequestParam(value ="coin", required =true) String coin){
        return markets.getMarket(coin.toUpperCase()).getAdx().getDXqueueAsString();
    }


    @GetMapping("marketList")
    public String getAllCoins(){
        return markets.getMarketBooks().keySet().toString();
    }

    @GetMapping("/keltner")
    public String getKeltner(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin.toUpperCase()).getKeltnerChannels().toString();
    }

    @GetMapping("/keltnerQ")
    public String getKeltnerQueues(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin.toUpperCase()).getKeltnerChannels().getQueues();
    }

    @GetMapping("/dmcomponents")
    public String getDMcomponents(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin.toUpperCase()).getAdx().getDMcomponents();
    }


    @GetMapping("/negativedirection")
    public String getNegativeDirection(){
        return markets.getANegativeDirection();
    }


    @GetMapping("/pandl")
    public String getPandL(){
        return String.format("Current Value: %f<br><br>P and L:%f"
                , portFolio.getCurrentPortFolioValue(), portFolio.profitAndLossPercentage());
    }


    @GetMapping("/hodl")
    public String getHodl(){
        StringBuilder b = new StringBuilder();
        Positions positions = portFolio.getPositionManager().getPositionBooks();
        Set<String> list = positions.keySet();
        for(String s: list){
            b.append(positions.get(s).toString()+"</br>");
        }
        return b.toString();

    }


}
