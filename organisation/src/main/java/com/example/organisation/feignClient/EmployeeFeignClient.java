package com.example.organisation.feignClient;

import com.example.organisation.dto.EmployeesDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//To get the url from discovery client you have to register your service to eureka else put the url here
//@FeignClient(name ="EMPLOYEE-SERVICE", /*url = "http://localhost:8081",*/path = "/empController"/*,fallback = FallBackEmployeeResponse.class*/)

/*In idle scenario every request should route through gateway, even if the communication is between two microservices
Hence in feignClient we will specify gateway name provided in eureka rather than direct service name*/
@FeignClient(name ="API-GATEWAY",path = "/empController")
public interface EmployeeFeignClient {
  @GetMapping(value = "/getAll")
  public ResponseEntity<List<EmployeesDTO>> getAllEmployee();
  }
  
/*
  One thing to remember, suppose from our this service we have to connect with few other services too, then we
 have to create feign clients for those services too and give it a name, but as we are using gateway, and
 it's name in the @FeignClient, we cannot create other feignClient with the same name. So the best way is to
just create one generic feignclient and give the gateway name and inside that only create all the endpoints of
different services,
and remove the path variable and append the path in each endpoint separately.
Remove path from @FeignClient(name ="API-GATEWAY",path = "/empController"), and add the path in
  @GetMapping(value = "/empController/getAll"), Similarly add the path for other services in their respective mappings, like
  @GetMapping(value = "/abcController/getFirstValue")*/
