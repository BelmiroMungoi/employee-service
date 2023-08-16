package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
