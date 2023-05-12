package com.example.employee.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
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
  private Integer empid;

  @Column(name = "empName")
  private String empName;

  @Column(name = "status")
  private String status;

  @Column(name = "salary")
  private Integer salary;

  @Column(name = "date_created",updatable = false)
  @CreationTimestamp
  private LocalDate date_created;

  @Column(name = "date_updated",insertable = false)
  @UpdateTimestamp
  private LocalDate date_updated;
}
