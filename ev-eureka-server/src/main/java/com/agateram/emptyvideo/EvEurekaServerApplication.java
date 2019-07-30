package com.agateram.emptyvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EvEurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EvEurekaServerApplication.class, args);
	}
}
