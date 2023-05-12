package com.example.student.service;

import java.util.List;

import com.example.student.studentModel.StudentResponse;
import com.example.student.entity.Student;
import com.example.student.exception.customException.ResourceNotFound;
import com.example.student.repository.StudentRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  public Student saveStudent(Student student) {
    return studentRepo.save(student);
  }

  @Override
  public List<Student> getAllStudents() {
    List<Student> studentList = studentRepo.findAll();
    return studentList;
  }

  public StudentResponse getStudentById(int id) {
    //        Optional<Employees> employees= employeeRepo.findById(id);
    //        if(employees.isPresent()){
    //            return employees.get();
    //        }
    //        else {
    //            throw new ResourceNotFound("Employee", "id", id);
    //        }
    // same in lambda
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
