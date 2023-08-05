package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Mission;
import com.bbm.employeeservice.model.MissionStatus;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.model.dto.MissionResponse;
import com.bbm.employeeservice.model.dto.StatusResponse;
import com.bbm.employeeservice.repository.MissionRepository;
import com.bbm.employeeservice.repository.MissionStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionStatusRepository missionStatusRepository;

    public AppResponse createMission(MissionRequest request, Long userId) {
        User user = new User(userId);
        MissionStatus status = missionStatusRepository.findByStatus(request.getMissionStatus());
        Mission mission = Mission.builder()
                .missionName(request.getMissionName())
                .missionStatus(status)
                .startedAt(LocalDateTime.now())
                .finishedAt(request.getFinishedDate())
                .employees(null)
                .user(user)
                .build();
        missionRepository.save(mission);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Missão foi Salva com Sucesso")
                .name(mission.getMissionName())
                .build();
    }

    public Mission getMissionById(Long id, Long userId) {
        return missionRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new EntityNotFoundException("Missão com o ID: " + id + " não foi encontrado"));
    }

    public Set<Mission> getMissionByName(String mission, Long userId) {
        return missionRepository.findAllByMissionNameContainsIgnoreCaseAndUserId(mission, userId).orElseThrow(() ->
                new EntityNotFoundException("Missão com  nome: " + mission + "não foi encontrada"));
    }

    public List<MissionResponse> getAllMission(Long userId) {
        List<Mission> mission = missionRepository.findAllByUserId(userId);
        return mission.stream().map(this::mapToMissionResponse).toList();
    }

    public List<StatusResponse> getAllStatus() {
        List<MissionStatus> status = missionStatusRepository.findAll();
        return status.stream().map(this::mapToStatusResponse).toList();
    }

    public AppResponse updateMission(Long id, MissionRequest request, Long userId) {
        MissionStatus status = missionStatusRepository.findByStatus(request.getMissionStatus());
        Mission mission = getMissionById(id, userId);
        mission.setMissionName(request.getMissionName());
        mission.setMissionStatus(status);
        mission.setFinishedAt(request.getFinishedDate());
        missionRepository.save(mission);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Missão foi actualizada com sucesso")
                .name(mission.getMissionName())
                .build();
    }

    public void deleteMission(Long id, Long userId) {
        Mission mission = getMissionById(id, userId);
        missionRepository.delete(mission);
    }

    private MissionResponse mapToMissionResponse(Mission mission) {
        return MissionResponse.builder()
                .id(mission.getId())
                .missionName(mission.getMissionName())
                .startedDate(mission.getStartedAt())
                .finishedDate(mission.getFinishedAt())
                .missionStatus(StatusResponse.builder()
                        .missionStatus(mission.getMissionStatus().getStatus())
                        .build())
                .build();
    }

    private StatusResponse mapToStatusResponse(MissionStatus status) {
        return StatusResponse.builder()
                .missionStatus(status.getStatus())
                .build();
    }
}
