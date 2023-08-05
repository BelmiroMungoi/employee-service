package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionStatusRepository extends JpaRepository<MissionStatus, Long> {
    MissionStatus findByStatus(String missionStatus);
}
