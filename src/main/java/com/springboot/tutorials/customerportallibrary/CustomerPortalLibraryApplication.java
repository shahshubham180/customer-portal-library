package com.springboot.tutorials.customerportallibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.springboot.tutorials.customerportallibrary.customer.utils.CustomerUtils;


@SpringBootApplication
public class CustomerPortalLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerPortalLibraryApplication.class, args);
	}
	
	@Bean
	public CustomerUtils getCustomerUtils() {
		return new CustomerUtils();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
