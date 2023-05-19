package com.example.student.service;

import java.util.List;

import com.example.student.studentModel.StudentResponse;
import com.example.student.entity.Student;
import org.springframework.data.domain.Pageable;

public interface StudentService {

  Student saveStudent(Student student);

  List<StudentResponse> getAllStudents();

  StudentResponse getStudentById(int id);

  Student updateStudent(Student student, int id);

  void deleteStudent(int id);

  List<Student> studentPageable(Pageable pageable);
}
