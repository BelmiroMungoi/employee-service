package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByNameAndUserId(String name, Long userId);

    Page<Department> findAllByUserId(PageRequest pageRequest, Long userId);

    List<Department> findAllByUserId(Long userId);

    Page<Department> findAllByNameContainsIgnoreCaseAndUserId(PageRequest pageRequest, String name, Long userId);

    Optional<Department> findByIdAndUserId(Long id, Long userId);

    Integer countAllByUserId(Long userId);

    boolean existsByNameAndUserId(String name, Long userId);
}
