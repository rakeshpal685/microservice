package com.example.employee.service;

import java.util.Arrays;
import java.util.List;

import com.example.employee.EmployeeModel.EmployeesResponse;
import com.example.employee.EmployeeModel.StudentResponse;
import com.example.employee.entity.Employees;
import com.example.employee.exception.customException.ResourceNotFound;
import com.example.employee.repository.EmployeeRepo;
import com.example.employee.feignClient.StudentFeignClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
  @Autowired private EmployeeRepo employeeRepo;

  @Autowired
  /*We will use modelMapper to map our entity to the model mapper, for this we have to adda maven dependency
  and create a bean also */
  private ModelMapper modelMapper;

/*  We will set the RestTemplate value using constructor injection.
  @Autowired */
  private RestTemplate restTemplate;
  @Autowired private StudentFeignClient studentFeignClient;

/*  Here we are setting the RestTemplate and the base URL, so that we don't have to write the url every
  time we have to call the student API*/
  public EmployeeServiceImpl(@Value("${studentService.base.url.for.restTemplate}") String studentBaseUrl,
                             RestTemplateBuilder restTemplateBuilder) {
  this.restTemplate=restTemplateBuilder.rootUri(studentBaseUrl).build();
  }

  @Override
  public Employees saveEmployees(Employees employees) {
    return employeeRepo.save(employees);
  }

  @Override
  public List<EmployeesResponse> getAllEmployees() {
    List<Employees> employeesList = employeeRepo.findAll();
    //Here I am mapping the employees List to EmployeesResponse array and finally converting it to List
    List<EmployeesResponse> employeesResponse= Arrays.asList(modelMapper.map(employeesList, EmployeesResponse[].class));
    //Here I am setting the students also to the employees
    employeesResponse.forEach(employee -> {
           // employee.setStudentDetails(studentFeignClient.invokeStudentApi(employee.getEmpid())));
    for (StudentResponse response :studentFeignClient.getAllStudents()) {
        if(response.getId()==employee.getEmpid()){
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
    /*
    StudentResponse restTemplateForObject =
        restTemplate.getForObject(
            "http://localhost:8081/studController/getStudentById/{id}", StudentResponse.class, id);
    employeesResponse.setStudentDetails(restTemplateForObject);

    //We can do something like this also
    ResponseEntity<StudentResponse> forEntity = restTemplate.getForEntity("http://localhost:8081/studController/getStudentById/{id}", StudentResponse.class);
    employeesResponse.setStudentDetails(forEntity.getBody());

    If we don't want to hardcode out URL we can do this
      @Autowired LoadBalancerClient loadBalancerClient from spring framework
     ServiceInstance instance= loadBalancerClient.choose("Student-Service")/the name of the service given in eureka
String URI= instance.getUri().toString();
StudentResponse restTemplateForObject =
        restTemplate.getForObject(
            URI+"/studController/getStudentById/{id}", StudentResponse.class, id);
    employeesResponse.setStudentDetails(restTemplateForObject);
    OR ELSE
    use @LoadBalanced where we are declaring the RestTemplate bean rather than autowiring it here.
      */

    StudentResponse studentApi = studentFeignClient.invokeStudentApi(id);
    // System.out.println(studentApi);
    employeesResponse.setStudentDetails(studentApi);

    return employeesResponse;
  }
}
