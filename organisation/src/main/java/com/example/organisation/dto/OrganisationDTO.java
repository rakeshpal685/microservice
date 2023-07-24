package com.example.organisation.dto;


import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*This is the model class that is mapped to the entity, we will never be going to send the entity object
directly to the controller to prevent exposing our DB data directly to outside world via API call*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDTO {
  
  private OrganisationDetail organisationDetail;
  
  private List<EmployeesDTO> employeesDTOs;
  
}
