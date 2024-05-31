package com.dotnano.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryGatewayApplication.class, args);
	}

}
