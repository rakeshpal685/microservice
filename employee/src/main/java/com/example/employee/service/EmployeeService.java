package com.example.employee.service;


import java.util.List;

import com.example.employee.EmployeeModel.EmployeesResponse;
import com.example.employee.entity.Employees;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

   Employees saveEmployees(Employees employees);

    List<EmployeesResponse> getAllEmployees();

    EmployeesResponse getEmployeeById(int id);

  Employees updateEmployee(Employees employees, int id);

  void deleteEmployee(int id);

  List<Employees> employeesPageable(Pageable pageable);

    public EmployeesResponse getEmpAndStudent(int id);
}
