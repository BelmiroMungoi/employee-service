package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);
}
