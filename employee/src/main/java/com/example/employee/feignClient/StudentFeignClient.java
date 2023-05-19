package com.example.employee.feignClient;

import com.example.employee.EmployeeModel.StudentResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STUDENT-Service",path = "/studController")
/*This is the same name by which my Student service is registered in Eureka,Hence feign will go to eureka
and against the Application name it will fetch the available server address on which Student is running, and
on the available server it will do the below method calls, the path provided here is the context path or the
main controller path*/
public interface StudentFeignClient {
  /*This is an interface and JVM will create a proxy class in runtime to give implementation when we will use
  this interface to autowired in employee service*/

  @GetMapping("/getStudentById/{id}")
  StudentResponse invokeStudentApi(@PathVariable("id") int id);
  //  The above get call might return ResponseEntity<StudentResponse>, add .getBody() to just get the response.


  @GetMapping("/getAll")
 List<StudentResponse> getAllStudents();
}
