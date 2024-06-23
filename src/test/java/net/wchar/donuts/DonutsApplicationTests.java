package net.wchar.donuts;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

@SpringBootTest
class DonutsApplicationTests {

	protected final   Logger logger = LoggerFactory.getLogger(DonutsApplicationTests.class);

	@Autowired
	private MessageSource messageSource;
	@Test
	void contextLoads() {
		Locale locale=Locale.getDefault();
		String greeting=messageSource.getMessage("greeting",null,locale);
		logger.info(greeting);
	}

}
