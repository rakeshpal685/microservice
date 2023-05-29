package com.example.employee.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "employees")
public class Employees {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  //This is for swagger, so that this field is not visible when we are going for post request
  private Integer empid;

  @Column(name = "empName")
  private String empName;

  @Column(name = "status")
  private String status;

  @Column(name = "salary")
  private Integer salary;

  @Column(name = "date_created",updatable = false)
  @CreationTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  //This is for swagger, so that this field is not visible when we are going for post request
  private LocalDate date_created;

  @Column(name = "date_updated",insertable = false)
  @UpdateTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  //This is for swagger, so that this field is not visible when we are going for post request
  private LocalDateTime date_updated;
}
