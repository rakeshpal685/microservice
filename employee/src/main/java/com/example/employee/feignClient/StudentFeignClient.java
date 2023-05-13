package com.example.employee.feignClient;

import com.example.employee.EmployeeModel.StudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STUDENT")
public interface StudentFeignClient {

  @GetMapping("/studController/getStudentById/{id}")
  StudentResponse invokeStudentApi(@PathVariable("id") int id);
  // public String invokeStudentApi();
}
