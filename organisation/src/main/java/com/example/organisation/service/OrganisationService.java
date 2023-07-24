package com.example.organisation.service;

import com.example.organisation.dto.EmployeesDTO;
import com.example.organisation.feignClient.EmployeeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganisationService {
    
    @Autowired
    private EmployeeFeignClient employeeFeignClient;

    public  ResponseEntity<List<EmployeesDTO>> getAllEmployees(){
        ResponseEntity<List<EmployeesDTO>> allEmployee = employeeFeignClient.getAllEmployee();
        return allEmployee;
    }

}
