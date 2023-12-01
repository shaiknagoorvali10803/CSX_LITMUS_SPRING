package com.csx;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class SpringSeleniumApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SpringSeleniumApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {

	}
}
