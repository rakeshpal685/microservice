package com.example.employee.controller;

import com.example.employee.EmployeeModel.EmployeesRequest;
import com.example.employee.EmployeeModel.EmployeesResponse;
import com.example.employee.entity.Employees;
import com.example.employee.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    "/empController") // we can set the context root path in the properties file rather then using
// this here, both work as the entry path for the controller
@Log4j2
public class EmployeeController {

  @Autowired EmployeeService employeeService;

  /*  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }*/
  int retryCount =
      1; // Just to count the number of retry server does when we are using @Retry below

  @PostMapping(value = "/saveEmployeeAndStudent", consumes = "application/json")
  public ResponseEntity<EmployeesResponse> saveEmployeeAndStudent(
      @RequestBody EmployeesRequest employees) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.saveEmployeeAndStudent(employees));
  }

  /*  Here I am using ResponseEntity as the return type because it can give a lot of info back when
  this url is hit, like header, code etc. @PostMapping will map the request to the url and @RequestBody will
  take the  body of the request (json) and add to our method, (Internally DispatcherServlet will convert the json to
  java object and map it to the parameter of the method*/
  @PostMapping(value = "/save", consumes = "application/json")
  public ResponseEntity<EmployeesResponse> saveEmployee(@RequestBody Employees employees) {
    // return new ResponseEntity<>(employeeService.saveEmployees(employees), HttpStatus.CREATED);
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployees(employees));
  }

  @GetMapping(value = "/getAll")
  //@CircuitBreaker(name = "studentBreaker", fallbackMethod = "getAllEmployeeFallback") //We will write the circuitbreaker in service
  // Below annotations are for swagger
  @Operation(summary = "Get all employees") // Description of the controller method.
  @ApiResponses(
      value = { // what to show when we have different status codes.
        @ApiResponse(
            responseCode = "200",
            description = "Found all employees",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Employees.class))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
      })
  public ResponseEntity<List<EmployeesResponse>> getAllEmployee() {
    log.info("Inside Employee Controller's :{} method","getAllEmployee()");
    log.warn("Inside /getAll controller");
    return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees());
    /*    Rather than creating a new ResponseEntity<> object like below we can do this also*/
  }

  /*PathParameter can be present anywhere in the URl, we just have to keep it inside the {}, /getEmployeeById/{id}/data*/
  @GetMapping(
      value = "/getEmployeeById/{id}",
      produces = {
        "application/json",
        "application/xml"
      } // If our project has JAX-B dependency then it will return xml value.
      )
  public ResponseEntity<EmployeesResponse> getEmployeeById(@PathVariable("id") int empid) {
    return new ResponseEntity<EmployeesResponse>(
        employeeService.getEmployeeById(empid), HttpStatus.OK);
  }

  @GetMapping(value = "/getEmployeeById")
  /*Here I will use query parameter while calling the get mapping (http://localhost:8080/empController/getEmployeeById?id=5)
  anything after the ? will be treated as Query parameter and we have to use @RequestParam to capture the data, Query param will be present
  in key:value pair. We can have multiple parameters (they wil be separated by & in the url, http://localhost:8080/empController/getEmployeeById?id=5&userName=rakesh)
  and in that case we have to capture both the parameters using @RequestParam*/
  public ResponseEntity<EmployeesResponse> getEmployeeByIdUsingQueryParam(
      @RequestParam("id") Integer id) {
    return new ResponseEntity<EmployeesResponse>(
        employeeService.getEmployeeById(id), HttpStatus.OK);
  }

  @GetMapping(value = "/listPageable")
  // http://localhost:8080/empController/listPageable?page=0&size=2
  public ResponseEntity<List<Employees>> employeesPageable(Pageable pageable) {
    return new ResponseEntity<List<Employees>>(
        employeeService.employeesPageable(pageable), HttpStatus.OK);
  }

  @PutMapping("{id}")
  public ResponseEntity<Employees> updateEmployee(
      @PathVariable("id") int empid, @RequestBody Employees employee) {
    return new ResponseEntity<Employees>(
        employeeService.updateEmployee(employee, empid), HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") int empid) {
    employeeService.deleteEmployee(empid);
    return new ResponseEntity<String>("Employee deleted successfully", HttpStatus.OK);
  }

  @GetMapping("/getEmpAndStudent/{id}")
  /*Add the resilience4j dependency, it provides us 3 types of resilience
   1. CircuitBreaker
   2.Retry
   3.RateLimiter
   we have to use the below annotation with any name you like and a fallback method,
  this method will be called in case out destination service is down, we have to use circuit breakers only on those
  methods where we are calling some other service, the name can be anything and this name is used in .properties
  file for resilience4j.circuitbreaker.instances property
    We can write teh circuit breaker code on controller,service or on feignclient too*/

  @CircuitBreaker(name = "studentBreaker", fallbackMethod = "studentFallbackMethod")

  /*We can implement retry module also, to ping the student service in-case it is down. we have to make configuration in
   properties file for the retry things and same as above we will call the fallback method in-case retry fails.
   Few things to consider..
   1. We should keep in mind that whether to retry or the error should be logged in-case the service is failed, like
   Retry for - Connection timeout
   Don't retry for - NullPointerException, Authentication failure
   2. How long should we keep retrying.
   Sometimes during web request an error is better than slow response.
   But for background tasks like report generation, email notification, retry is worthy
   3. How frequently we should retry
   We have different mechanism for the time, Constant/Incremental(Exponential)/Random
   4. Always have a fallback method to exit gracefully.

  @Retry(name = "studentServiceRetry", fallbackMethod = "studentFallbackMethod")  */

  // @RateLimiter(name = "presentServiceRateLimiter", fallbackMethod = "studentFallbackMethod")
  public ResponseEntity<EmployeesResponse> getEmpAndStudent(@PathVariable("id") int empid) {
    log.warn("Retry count: {}", retryCount);
    retryCount++;
    return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmpAndStudent(empid));
  }

  /*creating fallback method for circuit breaker,things to keep in mind while creating the fallback method
      1) The method has to be in the same class,
      2) The return type should be same as the base method where fallback is declared
      3) If there are any parameters in the base method then the parameters are also should be same,
      4) fallback method must have a parameter of type throwable, if we want we can catch the exception later
  */
  public ResponseEntity<EmployeesResponse> studentFallbackMethod(int empid, Exception e) {
    log.info("Fallback is executed because student service is down: " + e.getMessage());
    EmployeesResponse response =
        EmployeesResponse.builder()
            .empName("Random, Because Student Service is down")
            .salary(10)
            .empid(empid)
            .status("F")
            .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
