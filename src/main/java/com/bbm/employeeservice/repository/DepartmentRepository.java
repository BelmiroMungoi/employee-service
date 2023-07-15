package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByNameAndUserId(String name, Long userId);
    List<Department> findAllByUserId(Long userId);
    List<Department> findAllByNameContainsIgnoreCaseAndUserId(String name, Long userId);
    Optional<Department> findByIdAndUserId(Long id, Long userId);
}
