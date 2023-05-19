package com.example.gateway.springcloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/*Whenever a request comes to the API gateway, two things will happen,routing will be performed and the filters will
be executed, the routing will route the incoming request to the specific microservice and the filter will intercept
the request and perform the logic that we can write, routing will be done only once it passes the conditions of the filter, we can check many things about the request here,like from where the data
is coming, what's in the header or body, we can perform validations too and many more things. we can implement the Security here.
If */

@Component
public class MyFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("filter executed...");
        ServerHttpRequest request = exchange.getRequest();//It is used to capture the complete request coming fom the client
        HttpHeaders headers = request.getHeaders();
/*request.getPath();
request.getBody();*/

        Set<String> keySet = headers.keySet();//to get all the keys in the headers

        for (String key : keySet) {
            System.out.print(key+"---");
            System.out.println(headers.getValuesAsList(key));
        }

        return chain.filter(exchange);
    }
}
