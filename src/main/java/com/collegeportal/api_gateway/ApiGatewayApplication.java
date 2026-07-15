package com.collegeportal.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
@EnableDiscoveryClient // <-- ADD THIS ANNOTATION
public class ApiGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("api gateway is runnig");
	}

	@Bean
	@org.springframework.cloud.client.loadbalancer.LoadBalanced
	public org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder() {
		return org.springframework.web.reactive.function.client.WebClient.builder();
	}

}