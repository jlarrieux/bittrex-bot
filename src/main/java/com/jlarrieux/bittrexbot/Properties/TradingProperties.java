package com.jlarrieux.bittrexbot.Properties;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Data
@Component
@ConfigurationProperties("trading")
public class TradingProperties {

    private double stopLoss, profitTaking, spread, rsiOverBought, rsiOverSold, rsiNoMomentum;


}
