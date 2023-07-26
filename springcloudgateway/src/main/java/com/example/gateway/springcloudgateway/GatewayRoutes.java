package com.example.gateway.springcloudgateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

//This class is created to handle the routes, we can do the same in .yml/properties files too, here we are doing it in java way
@Configuration
@Log4j2
public class GatewayRoutes {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    log.info("Inside prefilter of GatewayRoutes class");
    return builder
        .routes()
    /*So when a request comes to our gateway that matches with the condition given in the predicate (p in this case),
the gateway will go to the Eureka and choose one service from EMPLOYEE-SERVICE (uri) in loadbalanced way and redirect the call
Similarly we can have p.cookies too, here we are telling that if my request has the below cookie then navigate the request to the uri*/
        .route(
            "EMPLOYEE-App-Route",p -> p.path("/empController/**")
                // Here I am creating a filter and adding a custom responseHeader, when sending response back to the user via gateway
                                            .filters(f -> f.addResponseHeader("Company-Name","Rakesh "))
                                            .uri("lb://EMPLOYEE-SERVICE"))
        .route("AnyRandomID", p -> p.path("/studController/**").uri("lb://STUDENT-SERVICE"))
        
        .build();
  }

/*   Creating a filter using java @Bean approach rather than creating a new class totally, we can create many
  filter bean like this*/
  @Order(3)
  @Bean
  public GlobalFilter xyzFilter() {
    return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
      // We can do anything that we want to do here in prefilter
      log.info("Inside prefilter of xyzFilter- order 3");
      return chain.filter(exchange);
      // This is for post filter, just add ".then(Mono.fromRunnable(()->{" to the above return
      // statement and write the post filter logic
      /*    return chain.filter(exchange).then(Mono.fromRunnable(()->{
        //post filter stuffs
        log.info("Status code received "+ exchange.getResponse().getStatusCode());
        System.out.println("Inside postfilter of xyzFilter- order 3");
      }));*/

    };
  }
}
