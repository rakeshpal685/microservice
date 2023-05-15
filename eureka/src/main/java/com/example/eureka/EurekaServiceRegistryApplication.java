package com.example.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //when this annotation is enabled then Spring will create a bean with the name Marker and trigger
//EurekaServerAutoConfiguration class, in which it will check if there is any class called Marker present or not,
//if present then it will configure the eureka server for us.
public class EurekaServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceRegistryApplication.class, args);
	}

}
