package com.example.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/*
My config server will connect to third party to get the properties from there, It can be GIT, LDAP, local system
anything, my microservice will connect to this config server which in turns will connect to the source for the properties
*/
@SpringBootApplication
@EnableConfigServer//To tell that this is a config server that will read the properties from outside
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
