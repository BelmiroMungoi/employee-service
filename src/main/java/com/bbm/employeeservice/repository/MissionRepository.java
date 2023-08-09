package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Page<Mission>> findAllByMissionNameContainsIgnoreCaseAndUserId(PageRequest pageRequest, String missionName, Long userId);
    Optional<Set<Mission>> findByMissionNameAndUserId(String missionName, Long userId);
    Optional<Mission> findByIdAndUserId(Long id, Long userID);
    Page<Mission> findAllByUserId(PageRequest pageRequest, Long userId);
}
