package com.arg.swu.digitalsignatures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DigitalsignaturesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalsignaturesApplication.class, args);
	}

}
