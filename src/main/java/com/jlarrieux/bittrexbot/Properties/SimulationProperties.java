package com.jlarrieux.bittrexbot.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:simulation.properties")
@ConfigurationProperties(prefix = "simulation")
public class SimulationProperties {

   private boolean useBorderDates;
    private String jdbcDriver;
    private String connection_url;
    private String user;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;
    private int acquireIncrement;
    private double coinQuantity;
    private String dateFormatter;
    private String timeFormatter;
    private String startDate;
    private String endDate;
    private String marketNames;
}
