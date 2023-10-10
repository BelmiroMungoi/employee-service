package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Mission;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.*;
import com.bbm.employeeservice.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mission")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createMission(@Valid @RequestBody MissionRequest request,
                                                     @AuthenticationPrincipal User authenticatedUser) {
        var mission = missionService.createMission(request, authenticatedUser.getId());
        return new ResponseEntity<>(mission, HttpStatus.CREATED);
    }

    @GetMapping("/page/{page}")
    @CacheEvict(value = "missions", allEntries = true)
    @CachePut("missions")
    public ResponseEntity<Page<MissionResponse>> getAllMission(@PathVariable("page") int page,
                                                               @AuthenticationPrincipal User authenticatedUser) {
        Page<MissionResponse> missions = missionService.getAllMission(page, authenticatedUser.getId());
        return new ResponseEntity<>(missions, HttpStatus.OK);
    }

    @GetMapping("/name/{name}/page/{page}")
    @CacheEvict(value = "missions", allEntries = true)
    @CachePut("missions")
    public ResponseEntity<Page<MissionResponse>> getAllMissionByName(@PathVariable("name") String name,
                                                                     @PathVariable("page") int page,
                                                                     @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.getAllMissionByName(page, name, authenticatedUser.getId()));
    }

    @GetMapping("/employee/{employeeId}/page/{page}")
    @CacheEvict(value = "missionWithEmployee", allEntries = true)
    @CachePut("missionWithEmployee")
    public ResponseEntity<Page<MissionResponse>> getAllMissionByEmployeeId(@PathVariable("employeeId") Long emplooyeeId,
                                                                           @PathVariable("page") int page,
                                                                           @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.getAllMissionByEmployeeIdAndUserId(page, emplooyeeId, authenticatedUser.getId()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MissionResponse> getMissionById(@PathVariable("id") Long id,
                                                               @AuthenticationPrincipal User authenticatedUser){
        Mission mission = missionService.getMissionById(id, authenticatedUser.getId());
        return ResponseEntity.ok(missionService.mapToMissionResponse(mission));
    }

    @GetMapping("/status")
    @CacheEvict(value = "status", allEntries = true)
    @CachePut("status")
    public ResponseEntity<List<StatusResponse>> getAllStatus() {
        return ResponseEntity.ok(missionService.getAllStatus());
    }

    @GetMapping("/quantity")
    @CacheEvict(value = "quantity", allEntries = true)
    @CachePut("quantity")
    public ResponseEntity<Integer> getMissionByUserId(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.getMissionQuantityByUser(authenticatedUser.getId()));
    }

    @GetMapping("/statusChart")
    public ResponseEntity<StatusChartResponse> missionStatusChart(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.missionStatusChart(authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateMission(@PathVariable Long id, @Valid @RequestBody MissionRequest request,
                                                     @AuthenticationPrincipal User authenticatedUser) {
        var mission = missionService.updateMission(id, request, authenticatedUser.getId());
        return new ResponseEntity<>(mission, HttpStatus.OK);
    }

    @PutMapping("/employee/{employeeId}/mission/{missionId}")
    public ResponseEntity<AppResponse> addEmployeeToMission(@PathVariable("missionId") Long missionId,
                                                            @PathVariable("employeeId") Long employeeId,
                                                            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.addEmployeeToMission(missionId, employeeId, authenticatedUser.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMission(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        missionService.deleteMission(id, authenticatedUser.getId());
        return new ResponseEntity<>("Missão com o ID: " + id + " foi deletada com sucesso!", HttpStatus.OK);
    }
}
