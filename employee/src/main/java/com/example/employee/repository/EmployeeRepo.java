package com.example.employee.repository;

import java.util.List;
import java.util.Optional;

import com.example.employee.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employees, Integer> {

  /* @Query("SELECT date_created FROM Employees  ORDER BY date_updated DESC")
  List<Employees> lastRecord();

  @Query("Select s.empName from Employee s where s.status = ?1")
  List<String> getEmployeeFirstNameByStatus(String status);

  */
  /*If we have some complex query that we cannot define by using JPQL then we can use DB native query also
   * we have to use native=true with the query*/
  /*
   @Query("Select u from Employee u where u.empid = :id and u.empName=:name")
   List<Employees> getEmployeeByEmployeeIdAndName(
           @Param("id") Long empId, @Param("name") String name);

   @Query(
           value = "Select * from tbl_student u where u.student_id = :id and u.first_name=:name",
           nativeQuery = true)
   List<Employees> getEmployeesByEmployeeIdAndNameNative(
           @Param("id") Long empId, @Param("name") String name);


  */
  /* SELECT TableName1.columnName, TableName2.columnName, TableName1.columnName FROM TableName1
  INNER JOIN TableName2 ON TableName1.primaryKey=TableName2.primaryKey
  Where Table1/2.primaryKey=valueYou want to search (@Param("value));
  This is for inner join, first select the total columns you want to display then join both the tables
  based on first table primary key and second table primary key*/

 /* @Query(
      value =
          "SELECT ea.id, ea.age, ea.date_created, ea.date_updated, ea.student_first_name,\n"
              + "ea.student_last_name FROM microservices.student ea join microservices.employees e on \n"
              + " e.id= ea.emp_id  where ea.emp_id :id",
      nativeQuery = true)
  Optional<Employees> getEmplo(@Param("id") int empId);*/





}
