package com.example.gateway.springcloudgateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//We can create a filter using java @Bean approach rather than creating a new class totally, see GatewayRoutes for more

@Order(1)
/*@Order is used To execute this filter first in the list of filter chain, As we saw in the official documentation, prefilters
will execute first when the request comes from gateway to our service, then it reaches our service and when returning
the response back to the client the post filters will execute, the prefilter will execute in the given order. i.e, 1 then 2,
but the postfilter will execute in the reverse order i.e, 2 then 1*/
@Component
@Log4j2
public class AuthorizationFilter implements GlobalFilter {
  @Override
  /*ServerWebExchange can be treated as Request/Response model and GatewayFilterChain is the chain of filters*/
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    // Here I am capturing the authorization header from the request which may contains the JWT, and
    // we can do the user
    // validations
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    log.info("Authorization header token is " + authHeader);
    return chain.filter(exchange);
  }
}
