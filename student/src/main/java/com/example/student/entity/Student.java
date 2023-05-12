package com.example.student.entity;

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
public class Student {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String studentFirstName;

  private String studentLastName;

  private Integer age;

  @Column(name = "date_created", updatable = false)
  @CreationTimestamp
  private LocalDate date_created;

  @Column(name = "date_updated", insertable = false)
  @UpdateTimestamp
  private LocalDate date_updated;
}
