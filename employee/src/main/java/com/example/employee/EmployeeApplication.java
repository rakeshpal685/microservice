package com.example.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
/*This helps us to scan for the feignClient and generate an implementation during runtime,
 this will scan the root package for the classes with @FeignClient,in case if base package is not specified*/
@EnableFeignClients(basePackages = "com/example/employee/feignClient")
//If the feignClient is present in the base package then no need to give the path here.
public class EmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}

}
