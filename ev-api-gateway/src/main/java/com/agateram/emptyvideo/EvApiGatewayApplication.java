package com.agateram.emptyvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EvApiGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(EvApiGatewayApplication.class, args);
	}
}
