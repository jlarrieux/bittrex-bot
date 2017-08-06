package com.jlarrieux.bittrexbot.Properties;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("bittrex")
public class BittrexProperties {



    private String secret;
    private String key;

}
