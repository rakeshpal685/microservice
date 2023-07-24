package com.example.gateway.springcloudgateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/*Whenever a request comes to the API gateway, two things will happen,routing will be performed and the chain of filters will
be executed, the routing will route the incoming request to the specific microservice via Eureka and the filter will intercept
the request and perform the logic that we can write, routing will be done only once it passes the conditions of the filter,
we can check many things about the request here,like from where the data is coming, what's in the header or body,
we can perform validations too and many more things. we can implement the Security here.
Here we are creating our custom filter and attaching it to the GatewayFilterChain, so my filter will also be executed along with the
other filter chain given by Spring*/

//We can create a filter using java @Bean approach rather than creating a new class totally, see GatewayRoutes for more
@Order(2)
/*@Order is used To execute this filter second in the list of filter chain, As we saw in the official documentation, prefilters
will execute first when the request comes from gateway to our service, then it reaches our service and when returning
the response back to the client the post filters will execute, the prefilter will execute in the given order. i.e, 1 then 2,
but the postfilter will execute in the reverse order i.e, 2 then 1*/
@Component
@Log4j2
public class FilterToTraceLoggingDetails implements GlobalFilter {
  @Override
  /*ServerWebExchange can be treated as Request/Response model and GatewayFilterChain is the chain of filters*/
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    System.out.println("filter executed...");
    ServerHttpRequest request = exchange.getRequest(); // It is used to capture the complete request coming fom the client
    HttpHeaders headers = request.getHeaders(); // To get the headers of the request going through my filter
    RequestPath path = request.getPath(); // To get the path of the request going through my filter
    /* request.getBody();//To get the body of the request going through my filter*/
    log.info("This Path is called: "+path);
    log.info("----------------------------------------------------------------");
    log.info("Below are the request Header details");
    Set<String> keySet = headers.keySet(); // to get all the keys in the headers
   /* for (String key : keySet) {
      System.out.print(key + "---");
      //log.info(key + "---");
      System.out.println(headers.getValuesAsList(key));
      //log.info("headers.getValuesAsList(key)");
    }*/
    //Sending my request to the next filter in chain and finally hitting the service
    return chain.filter(exchange);
    
    //This is for post filter, just add ".then(Mono.fromRunnable(()->{" to the above return statement and write the post filter logic
/*    return chain.filter(exchange).then(Mono.fromRunnable(()->{
      //post filter stuffs
      log.info("Status code received "+ exchange.getResponse().getStatusCode());
    }));*/
    
  }
}
