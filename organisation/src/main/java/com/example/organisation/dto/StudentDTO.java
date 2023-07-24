package com.example.organisation.dto;

import java.time.LocalDate;
import lombok.Data;

/*In this case we need the StudentMapper, because we want to send the student data along with employee data
* and hence we will pass the DB values of the student to the employee Mapper.
* In the service we will make a RestTemplate call to the student API and add that to the employee mapper too*/
@Data
public class StudentDTO {
  private Integer id;

  private String studentFirstName;

  private String studentLastName;

  private Integer age;

  private LocalDate date_created;

  private LocalDate date_updated;
}
