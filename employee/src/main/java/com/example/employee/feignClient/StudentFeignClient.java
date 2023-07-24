package com.example.employee.feignClient;

import com.example.employee.EmployeeModel.EmployeesResponse;
import com.example.employee.EmployeeModel.StudentResponse;

import java.util.ArrayList;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//@FeignClient(name = "STUDENT-SERVICE", path = "/studController")
/*This is the same name by which my Student service is registered in Eureka,Hence feign will go to eureka
and against the Application name it will fetch the available server address on which Student is running, and
on the available server it will do the below method calls, the path provided here is the context path or the
main controller path

Add the dependency for the openfeign, we can also see that a parent pom is added for this in dependencyManagement
section, as openfeign is not an inbuilt project of spring, on top we can see the <spring-cloud.version>, from
where we will get the versions of the cloud dependencies
add @EnableFeignClients(basePackages = "com/example/employee/feignClient") in the main class

Similarly, we can create another feignClient for some other service too, if we want to communicate with that
service*/


/*In idle scenario every request should route through gateway, even if the communication is between two microservices
Hence in feignClient we will specify gateway name provided in eureka rather than direct service name*/
@FeignClient(name = "API-GATEWAY", path = "/studController")

public interface StudentFeignClient {
  /*This is an interface and JVM will create a proxy class in runtime to give implementation when we will use
  this interface to autowired in employee service*/

  @GetMapping("/getStudentById/{id}")
  /* public StudentResponse getStudentById(@PathVariable("id") int id);

  As this is an interface we cannot write the body of the method, spring will create a proxy implementation
  and inject the response to the Autowired variable in the EmployeeServiceImpl, where we have declared StudentFeignClient.

  If you have hold of the other service too, just copy the whole method signature as it is and paste it here,
  Here I have the code of the Student controller's getStudentById method, hence I just pasted the same here,
  If not then we have to ask what is the end point and what is the return type*/ ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") int stdId);

  @GetMapping("/getAll")
 // @CircuitBreaker(name = "studentBreaker", fallbackMethod = "studentFallbackMethod")
  ResponseEntity<List<StudentResponse>> getAllStudents();

  @PostMapping(value = "/save", consumes = "application/json")
  ResponseEntity<StudentResponse> saveStudent(@RequestBody StudentResponse student);
  
//  This is my fallback method, we have to declare it as default because this is an interface
  default  ResponseEntity<List<StudentResponse>> studentFallbackMethod(Throwable e) {
    return ResponseEntity.ok(new ArrayList<>());
  }
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