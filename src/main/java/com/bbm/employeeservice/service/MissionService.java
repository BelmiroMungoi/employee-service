package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Mission;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.model.dto.MissionResponse;
import com.bbm.employeeservice.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    public AppResponse createMission(MissionRequest request) {
        Mission mission = Mission.builder()
                .missionName(request.getMissionName())
                .duration(request.getMissionDuration())
                .employees(null)
                .build();
        missionRepository.save(mission);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Missão foi Salva com Sucesso")
                .name(mission.getMissionName())
                .build();
    }

    public Mission getMissionById(Long id) {
        return missionRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Missão com o ID: " + id + " não foi encontrado"));
    }

    public Set<Mission> getMissionByName(String mission) {
        return missionRepository.findAllByMissionName(mission).orElseThrow(() ->
                new EntityNotFoundException("Missão com  nome: " + mission + "não foi encontrada"));
    }

    public List<MissionResponse> getAllMission() {
        List<Mission> mission = missionRepository.findAll();
        return mission.stream().map(this::mapToMissionResponse).toList();
    }

    public AppResponse updateMission(Long id, MissionRequest request) {
        Mission mission = getMissionById(id);
        mission.setMissionName(request.getMissionName());
        mission.setDuration(request.getMissionDuration());
        missionRepository.save(mission);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Missão foi actualizada com sucesso")
                .name(mission.getMissionName())
                .build();
    }

    public void deleteMission(Long id) {
        Mission mission = getMissionById(id);
        missionRepository.delete(mission);
    }

    private MissionResponse mapToMissionResponse(Mission mission) {
        return MissionResponse.builder()
                .id(mission.getId())
                .missionName(mission.getMissionName())
                .missionDuration(mission.getDuration())
                .build();
    }
}
