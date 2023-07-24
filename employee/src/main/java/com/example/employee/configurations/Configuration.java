package com.example.employee.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  //We have used LoadBalanced here to tell spring that when I am using RestTemplate, do the loadbalancing by yourself
  @LoadBalanced
  //Our eureka-client dependency have inbuilt load balancer dependency, hence we have client side load balancer
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
/*  Add Webflux dependency for this
  public WebClient webClient() {
    return new WebClient();
  }*/
}
