package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
}
