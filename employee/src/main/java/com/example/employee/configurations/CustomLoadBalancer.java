/*
package com.example.employee.configurations;

import feign.Feign;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;

@LoadBalancerClient(
    value = "STUDENT-SERVICE",
    configuration = MyCustomLoadBalancerConfiguration.class)
public class CustomLoadBalancer {

  @LoadBalanced
  @Bean
  public Feign.Builder feignBuilder() {
    return Feign.builder();
  }
}
*/
