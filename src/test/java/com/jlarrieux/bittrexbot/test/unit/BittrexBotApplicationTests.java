package com.jlarrieux.bittrexbot.test.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class BittrexBotApplicationTests {

	@Autowired
	ApplicationContext context;


	@Test
	public void contextLoads() {


//		final ExchangeInterface exchangeInterface = context.getBean(ExchangeInterface.class);
//		org.junit.Assert.assertTrue(exchangeInterface instanceof SimulatorExchange);
	}



}
