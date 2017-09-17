package com.jlarrieux.bittrexbot.REST.Controller;



import com.jlarrieux.bittrexbot.UseCaseLayer.Decider;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.MarketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/")
public class MyController {

    private Decider decider;

    private MarketManager markets;

    @Autowired
    public MyController(Decider decider, MarketManager marketManager){
        this.decider = decider;
        this.markets = marketManager;
    }


//    @GetMapping(path = "/portfolio")
//    public @ResponseBody String getPortfolio(){
//        return  decider.getPortfolioStatement().toString();
//    }

    @GetMapping("/getcoin")
    public String getCoinInsight(@RequestParam(value = "coin", required = true) String coin){
        return markets.getCoinInsight(coin);
    }


    @GetMapping("/adx")
    public String getADX(@RequestParam(value = "coin", required = true) String coin){
        return markets.getMarket(coin).getAdx().toString();
    }


    @GetMapping("/dx")
    public String getDx(@RequestParam(value ="coin", required =true) String coin){
        return markets.getMarket(coin).getAdx().getDXqueueAsString();
    }


    @GetMapping("marketList")
    public String getAllCoins(){
        return markets.getMarketBooks().keySet().toString();
    }

    @GetMapping("/keltner")
    public String getKeltner(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin).getKeltnerChannels().toString();
    }

    @GetMapping("/keltnerQ")
    public String getKeltnerQueues(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin).getKeltnerChannels().getQueues();
    }

    @GetMapping("/dmcomponents")
    public String getDMcomponents(@RequestParam(value = "coin", required = true)String coin){
        return markets.getMarket(coin).getAdx().getDMcomponents();
    }



}
