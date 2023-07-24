/*
package com.example.employee.configurations;

import feign.Feign;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//This is just POC to change the default loadbalancer from roundrobin to random calling
@LoadBalancerClient(
    // Name of the service for which this load balancer will work
    value = "STUDENT-SERVICE",
//name of the configuration file where load balancing algorithm is defined, by default it is round robin.
    configuration = MyCustomLoadBalancerConfiguration.class)
public class CustomLoadBalancer {

  @LoadBalanced
  @Bean
  public Feign.Builder feignBuilder() {
    return Feign.builder();
  }
}
*/
