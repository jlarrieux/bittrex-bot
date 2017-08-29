package com.jlarrieux.bittrexbot;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;



@Log
@SpringBootApplication
@EnableScheduling
public class BittrexBotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(BittrexBotApplication.class, args);

	}


	@Configuration
	@ComponentScan(lazyInit = true)
	static class localConfig{

	}
}
