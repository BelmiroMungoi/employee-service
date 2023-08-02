package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);
    Optional<List<Employee>> findAllByFirstnameContainsIgnoreCaseAndUserId(String firstname, Long userId);
    Optional<Employee> findByIdAndUserId(Long employeeId, Long userId);
    List<Employee> findAllByUserId(Long userId);
    List<Employee> findAllByDepartmentNameAndUserId(String departmentName, Long userId);
    Integer countEmployeesByDepartmentNameContainsIgnoreCaseAndUserId(String department, Long userId);


}
