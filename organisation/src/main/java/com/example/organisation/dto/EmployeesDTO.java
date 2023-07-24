package com.example.organisation.dto;


import java.time.LocalDate;
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
public class EmployeesDTO {
  private Integer empid;

  private String empName;

  private String status;

  private Integer salary;

  private LocalDate date_created;

  private LocalDate date_updated;

  //Here we want to pass the student values also and hence we are mapping student mapper here.
 private StudentDTO studentDetails;
}
