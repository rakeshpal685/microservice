package com.example.student.service;

import java.util.Arrays;
import java.util.List;

import com.example.student.studentModel.StudentResponse;
import com.example.student.entity.Student;
import com.example.student.exception.customException.ResourceNotFound;
import com.example.student.repository.StudentRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);
  @Autowired private StudentRepo studentRepo;

  @Autowired
  /*We will use modelMapper to map our entity to the model mapper, for this we have to adda maven dependency
  and create a bean also */
  private ModelMapper modelMapper;

  @Override
  public StudentResponse saveStudent(Student student) {
    Student saved = studentRepo.save(student);
    return modelMapper.map(saved,StudentResponse.class);
    
  }

  @Override
  public List<StudentResponse> getAllStudents() {
    List<Student> studentList = studentRepo.findAll();
    List<StudentResponse> students = Arrays.asList(modelMapper.map(studentList, StudentResponse[].class));
    return students;

  }

  public StudentResponse getStudentById(int id) {
    Student student =
        studentRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Student", "id", id));
    // All the fields of our Employees entity will be mappd to the EmployeeResponse model
    StudentResponse studentResponse = modelMapper.map(student, StudentResponse.class);
    return studentResponse;
  }

  @Override
  public Student updateStudent(Student updatedStudentData, int id) {

    // We need to check if the id is present or not
    Student existingStudent =
        studentRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Student", "id", id));

    // Now we will set our data from the newly fetched data.
    existingStudent.setAge(updatedStudentData.getAge());
    existingStudent.setStudentFirstName(updatedStudentData.getStudentFirstName());
    existingStudent.setStudentLastName(updatedStudentData.getStudentLastName());

    // Now we will save the updated data
    studentRepo.save(existingStudent);
    return existingStudent;
  }

  @Override
  public void deleteStudent(int id) {
    studentRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Student", "id", id));
    studentRepo.deleteById(id);
  }

  @Override
  public List<Student> studentPageable(Pageable pageable) {
    return studentRepo.findAll(pageable).getContent();
  }
}
