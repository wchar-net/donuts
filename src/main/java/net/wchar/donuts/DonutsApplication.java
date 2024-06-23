package net.wchar.donuts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 主方法
 *
 * @author Elijah
 */
@EnableAsync
@SpringBootApplication
public class DonutsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonutsApplication.class, args);
	}

}
