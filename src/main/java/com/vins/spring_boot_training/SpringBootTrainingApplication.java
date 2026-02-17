package com.vins.spring_boot_training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTrainingApplication.class, args);
	}

}
