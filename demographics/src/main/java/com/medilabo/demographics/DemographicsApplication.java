package com.medilabo.demographics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemographicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemographicsApplication.class, args);
	}

}
//todo:  repository with one model for patients and one repo for patients, just one controller. view/get; use spring data