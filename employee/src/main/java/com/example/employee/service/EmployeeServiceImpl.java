package com.example.employee.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.employee.EmployeeModel.EmployeesRequest;
import com.example.employee.EmployeeModel.EmployeesResponse;
import com.example.employee.EmployeeModel.StudentResponse;
import com.example.employee.entity.Employees;
import com.example.employee.exception.customException.ResourceNotFound;
import com.example.employee.repository.EmployeeRepo;
import com.example.employee.feignClient.StudentFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class EmployeeServiceImpl implements EmployeeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
  @Autowired private EmployeeRepo employeeRepo;

  @Autowired
  /*We will use modelMapper to map our entity to the model mapper, for this we have to adda maven dependency
  and create a bean also */
  private ModelMapper modelMapper;

  /*  We will set the RestTemplate value using constructor injection.*/
  @Autowired private RestTemplate restTemplate;

  @Autowired private StudentFeignClient studentFeignClient;

  //  Use this if we are using RestTemplate to get the url of the Student service from Eureka, but
  // this will
  //  not work with load balancing
  @Autowired private DiscoveryClient discoveryClient;

  //  Use this for RestTemplate to get a client side load balancing when calling the other service
  @Autowired private LoadBalancerClient loadBalancerClient;

  /*  Here we are setting the RestTemplate and the base URL in the constructor, so that we don't have to write the url every
  time we have to call the student API*/
  /*public EmployeeServiceImpl(
      @Value("${studentService.base.url.for.restTemplate}") String studentBaseUrl,
      RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.rootUri(studentBaseUrl).build();
    // Here we are build the root uri which will be appended to the uri when we will be doing the
    // rest call
    // for Student in getEmpAndStudent() using rest Template
  }*/

  @Override
  public EmployeesResponse saveEmployees(Employees employees) {
    Employees saved = employeeRepo.save(employees);
    return (modelMapper.map(saved, EmployeesResponse.class));
  }

  public EmployeesResponse saveEmployeeAndStudent(EmployeesRequest employees) {
    Employees map = new ModelMapper().map(employees, Employees.class);
    Employees saved = employeeRepo.save(map);

    StudentResponse studentDetails = employees.getStudentDetails();
    studentFeignClient.saveStudent(studentDetails);

    return (modelMapper.map(saved, EmployeesResponse.class));
  }

  @Override
  @CircuitBreaker(name = "studentBreaker", fallbackMethod = "getAllEmployeeFallback") // we can write the callback directly in feignclient too.
  public List<EmployeesResponse> getAllEmployees() {
    List<Employees> employeesList = employeeRepo.findAll();
    // Here I am mapping the employees List to EmployeesResponse array and finally converting it to
    // List
    List<EmployeesResponse> employeesResponse =
        Arrays.asList(modelMapper.map(employeesList, EmployeesResponse[].class));
    // Here I am setting the students also to the employees
    employeesResponse.forEach(
        employee -> {
          // employee.setStudentDetails(studentFeignClient.invokeStudentApi(employee.getEmpid())));
          for (StudentResponse response : studentFeignClient.getAllStudents().getBody()) {
            if (response.getId() == employee.getEmpid()) {
              employee.setStudentDetails(response);
            }
          }
        });
    return employeesResponse;
  }

  public EmployeesResponse getEmployeeById(int id) {
    Employees employee =
        employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Employee", "id", id));
    // All the fields of our Employees entity will be mapped to the EmployeeResponse model
    return modelMapper.map(employee, EmployeesResponse.class);
  }

  @Override
  public Employees updateEmployee(Employees updatedEmployeeData, int id) {

    // We need to check if the id is present or not
    Employees existingEmployee =
        employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Employee", "id", id));

    // Now we will set our data from the newly fetched data.
    existingEmployee.setEmpName(updatedEmployeeData.getEmpName());
    existingEmployee.setStatus(updatedEmployeeData.getStatus());
    existingEmployee.setSalary(updatedEmployeeData.getSalary());

    // Now we will save the updated data
    employeeRepo.save(existingEmployee);
    return existingEmployee;
  }

  @Override
  public void deleteEmployee(int id) {
    employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Employee", "id", id));
    employeeRepo.deleteById(id);
  }

  @Override
  public List<Employees> employeesPageable(Pageable pageable) {
    return employeeRepo.findAll(pageable).getContent();
  }

  // RestTemplate is deprecated, and we don't use it anymore because the URL is hardcoded.
  @Override
  public EmployeesResponse getEmpAndStudent(int id) {
    Employees employee =
        employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Employee", "id", id));
    // All the fields of our Employees entity will be mapped to the EmployeeResponse model
    EmployeesResponse employeesResponse = modelMapper.map(employee, EmployeesResponse.class);

    // REST TEMPLATE IS NOT AT ALL RECOMMENDED
    /*    Get StudentResponse object using rest Template, Here we are using the api of Student microservice,
    to get the JSON output and use it in our employee microservice*/

    /*//    If we don't want to hardcode out URL and want to loadbalance the request from client side

         ServiceInstance instance= loadBalancerClient.choose("Student-Service")/the name of the service given in eureka or.properties
    String URI= instance.getUri().toString();
    //    Here we are getting the metadata that is present in the properties file of the StudentService, in the StudentService properties file we have
    //set some metadata that other services can ask for from outside the StudentService
    //    String contextRoot=serviceInstance.getMetaData().get("configPath");
    //We can set the contextRoot below in the url if it is declared in the properties file
        StudentResponse restTemplateForObject =restTemplate.getForObject(URI+"/studController/getStudentById/{id}", StudentResponse.class, id);
        employeesResponse.setStudentDetails(restTemplateForObject);
        OR ELSE
        use @LoadBalanced where we are declaring the RestTemplate bean rather than autowiring it here.

    //    Getting the URL of student service from Eureka only if one instance is running(not recommended)
        List<ServiceInstance> instances = discoveryClient.getInstances("STUDENT-Service");
        ServiceInstance serviceInstance=instances.get(0);
        String uri=serviceInstance.getUri().toString;

        //REMOVE ALL THE CODE FROM ABOVE AND JUST WRITE THE BELOW LINES, IT WILL LOAD-BALANCE ITSELF. JUST ADD
        //@LOADBALANCED IN THE CONFIG FILE WHERE WE ARE CREATING THE BEAN OF RESTTEMPLATE
        StudentResponse restTemplateForObject =restTemplate.getForObject("http://STUDENT-Service/studController/getStudentById/{id}", StudentResponse.class, id);
        employeesResponse.setStudentDetails(restTemplateForObject);
        */

    // Here I am getting ResponseEntity<StudentResponse>, hence used .getBody() to get the body.
    StudentResponse studentResponseBody = studentFeignClient.getStudentById(id).getBody();
    employeesResponse.setStudentDetails(studentResponseBody);

    return employeesResponse;
  }

  // Fallback method for /getAll controller
  public List<EmployeesResponse> getAllEmployeeFallback(Throwable e) {
    log.info(
        "Fallback is executed from EmployeeServiceImpl class as student service is down: "
            + e.getMessage());

    List<Employees> employeesList = employeeRepo.findAll();
    List<EmployeesResponse> employeesResponse =
        Arrays.asList(modelMapper.map(employeesList, EmployeesResponse[].class));
    // Here I am setting the students also to the employees
    employeesResponse.forEach(employee -> employee.setStudentDetails(new StudentResponse()));
    return employeesResponse;
  }
}
