package com.vendorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class VendorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendorServiceApplication.class, args);
	}

}
