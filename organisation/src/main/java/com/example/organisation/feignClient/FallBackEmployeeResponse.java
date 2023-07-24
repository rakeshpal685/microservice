/*
package com.example.organisation.feignClient;

import java.util.ArrayList;
import java.util.List;

import com.example.organisation.dto.EmployeesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

This is not required as we will implement fallback in the controller itself using @Circuitbreaker
@Component
public class FallBackEmployeeResponse implements EmployeeFeignClient {

  @Override
  public ResponseEntity<List<EmployeesDTO>> getAllEmployee() {

    ArrayList<EmployeesDTO> employeeList = new ArrayList<>();
    employeeList.add(new EmployeesDTO());

    return ResponseEntity.status(HttpStatus.OK).body(employeeList);
  }
}
*/
