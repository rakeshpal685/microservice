package com.example.student.controller;

import com.example.student.studentModel.StudentResponse;
import com.example.student.entity.Student;
import com.example.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studController")
public class StudentController {

  @Autowired StudentService studentService;

  @Autowired
  private Environment environment;

  /*  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }*/

  /*  Here I am using ResponseEntity as the return type because it can give a lot of info back when
  this url is hit, like header, code etc. @PostMapping will map the request to the url and @RequestBody will
  take the  body of the request (json) and add to our method, (Internally DispatcherServlet will convert the json to
  java object and map it to the parameter of the method*/
  @PostMapping(value = "/save", consumes = "application/json")
  public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
  }

  @GetMapping(value = "/getAll")
  // Below annotations are for swagger
  @Operation(summary = "Get all students") // Description of the controller method.
  @ApiResponses(
      value = { // what to show when we have different status codes.
        @ApiResponse(
            responseCode = "200",
            description = "Found all students",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Student.class))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
      })
  public ResponseEntity<List<StudentResponse>> getAllStudents() {
    return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
    /*    Rather than creating a new ResponseEntity<> object like below we can do this also*/
  }

  /*PathParameter can be present anywhere in the URl, we just have to keep it inside the {}, /getEmployeeById/{id}/data*/
  @GetMapping(
      value = "/getStudentById/{id}",
      produces = {
        "application/json",
        "application/xml"
      } // If our project has JAX-B dependency then it will return xml value.
      )
  public ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") int stdId) {
  //public String getStudentById(@PathVariable("id") int stdId) {
    return new ResponseEntity<StudentResponse>(studentService.getStudentById(stdId), HttpStatus.OK);
           // + environment.getProperty("server.port");
  }

  @GetMapping(value = "/getStudentById")
  /*Here I will use query parameter while calling the get mapping (http://localhost:8080/empController/getEmployeeById?id=5)
  anything after the ? will be treated as Query parameter and we have to use @RequestParam to capture the data, Query param will be present
  in key:value pair. We can have multiple parameters (they wil be separated by & in the url, http://localhost:8080/empController/getEmployeeById?id=5&userName=rakesh)
  and in that case we have to capture both the parameters using @RequestParam*/
  public ResponseEntity<StudentResponse> getStudentByIdUsingQueryParam(
      @RequestParam("id") Integer id) {
    return new ResponseEntity<StudentResponse>(studentService.getStudentById(id), HttpStatus.OK);
  }

  @GetMapping(value = "/listPageable")
  // http://localhost:8080/empController/listPageable?page=0&size=2
  public ResponseEntity<List<Student>> studentPageable(Pageable pageable) {
    return new ResponseEntity<List<Student>>(
        studentService.studentPageable(pageable), HttpStatus.OK);
  }

  @PutMapping("{id}")
  public ResponseEntity<Student> updateStudent(
      @PathVariable("id") int empid, @RequestBody Student student) {
    return new ResponseEntity<Student>(studentService.updateStudent(student, empid), HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteStudent(@PathVariable("id") int stdId) {
    studentService.deleteStudent(stdId);
    return new ResponseEntity<String>("Student deleted successfully", HttpStatus.OK);
  }
}
