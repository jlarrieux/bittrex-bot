package com.jlarrieux.bittrexbot;

import com.jlarrieux.bittrexbot.UseCaseLayer.Manager.BittrexDataManager;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;



@Log
@SpringBootApplication
@EnableScheduling
public class BittrexBotApplication /*implements CommandLineRunner*/{


	private BittrexDataManager bittrexDataManager;

	//@Autowired
	public BittrexBotApplication(BittrexDataManager bittrexDataManager) {
		this.bittrexDataManager = bittrexDataManager;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(BittrexBotApplication.class);
		//SpringApplication.run(BittrexBotApplication.class, args);

		/*System.out.println("Message from sysout");
		log.info("message from log info");*/
	}


	/*@Override
	public void run(String... args) throws Exception {

		while (true) {
			bittrexDataManager.getMarketSummaries();
		}
	}*/
}
