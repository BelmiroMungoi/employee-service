package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);
    Optional<Set<Employee>> findAllByFirstname(String firstname);
}
