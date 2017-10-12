package com.jlarrieux.bittrexbot;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;



@Log
@SpringBootApplication
@EnableScheduling
public class BittrexBotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(BittrexBotApplication.class);
		System.out.println("Message from sysout");
		log.info("message from log info");
	}



}
