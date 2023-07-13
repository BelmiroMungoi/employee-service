package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);
    Optional<Set<Employee>> findAllByFirstnameAndUserId(String firstname);
    Optional<Employee> findByIdAndUserId(Long employeeId, Long userId);
    List<Employee> findAllByUserId(Long userId);
    List<Employee> findAllByDepartmentNameAndUserId(String departmentName, Long userId);


}
