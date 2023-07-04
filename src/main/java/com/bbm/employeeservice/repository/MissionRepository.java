package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Set<Mission>> findAllByMissionName(String missionName);
}
