package com.example.student.studentModel;

import java.time.LocalDate;

import lombok.Data;

/*This is the model class that is mapped to the entity, we will never be going to send the entity object
directly to the controller to prevent exposing our DB data directly to outside world via API call*/
@Data
public class StudentResponse {
  private Integer id;

  private String studentFirstName;

  private String studentLastName;

  private Integer age;

  private LocalDate date_created;

  private LocalDate date_updated;
}
