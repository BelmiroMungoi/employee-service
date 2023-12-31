package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);

    Optional<Page<Employee>> findAllByFirstnameContainsIgnoreCaseAndUserId(PageRequest pageRequest, String firstname, Long userId);

    Optional<Employee> findByIdAndUserId(Long employeeId, Long userId);

    Page<Employee> findAllByUserId(PageRequest pageRequest, Long userId);

    List<Employee> findAllByDepartmentNameAndUserId(String departmentName, Long userId);

    @Query(nativeQuery = true, value = "SELECT e.* FROM employee AS e JOIN employee_mission AS " +
            "em ON e.id = em.employee_id WHERE em.mission_id = :missionId and e.user_id = :userId")
    Page<Employee> findAllByMissionIdAndUserId(PageRequest pageRequest, Long missionId, Long userId);

    @Query(nativeQuery = true, value = "SELECT e.* FROM employee AS e LEFT JOIN employee_mission AS " +
            "em ON e.id = em.employee_id AND em.mission_id = :missionId " +
            "WHERE em.mission_id IS NULL AND e.user_id = :userId")
    Page<Employee> findAllEmployeeWithoutThatMission(PageRequest pageRequest, Long missionId, Long userId);

    Integer countAllByUserId(Long userId);
}
