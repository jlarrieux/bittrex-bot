package com.jlarrieux.bittrexbot.REST.Controller;



import com.jlarrieux.bittrexbot.UseCaseLayer.Decider;
import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.MarketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/")
public class MyController {

    private Decider decider;

    private MarketManager markets;

    @Autowired
    public MyController(Decider decider, MarketManager marketManager){
        this.decider = decider;
        this.markets = marketManager;
    }


    @GetMapping(path = "/portfolio")
    public @ResponseBody String getPortfolio(){
        return  decider.getPortfolioStatement().toString();
    }

    @GetMapping("/getcoin")
    public @ResponseBody String getCoinInsight(@RequestParam(value = "coin", required = true) String coin){
        return markets.getCoinInsight(coin);
    }


}
