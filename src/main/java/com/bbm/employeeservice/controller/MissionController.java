package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Mission;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.model.dto.MissionResponse;
import com.bbm.employeeservice.model.dto.StatusResponse;
import com.bbm.employeeservice.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Page<MissionResponse>> getAllMission(@PathVariable("page") int page,
                                                               @AuthenticationPrincipal User authenticatedUser) {
        Page<MissionResponse> missions = missionService.getAllMission(page, authenticatedUser.getId());
        return new ResponseEntity<>(missions, HttpStatus.OK);
    }

    @GetMapping("/name/{name}/page/{page}")
    public ResponseEntity<Page<MissionResponse>> getAllMissionByName(@PathVariable("name") String name,
                                                                     @PathVariable("page") int page,
                                                                     @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.getAllMissionByName(page, name, authenticatedUser.getId()));
    }

    @GetMapping("/employee/{employeeId}/page/{page}")
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
    public ResponseEntity<List<StatusResponse>> getAllStatus() {
        return ResponseEntity.ok(missionService.getAllStatus());
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> getMissionByUserId(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(missionService.getMissionQuantityByUser(authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateMission(@PathVariable Long id, @Valid @RequestBody MissionRequest request,
                                                     @AuthenticationPrincipal User authenticatedUser) {
        var mission = missionService.updateMission(id, request, authenticatedUser.getId());
        return new ResponseEntity<>(mission, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMission(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        missionService.deleteMission(id, authenticatedUser.getId());
        return new ResponseEntity<>("Missão com o ID: " + id + " foi deletada", HttpStatus.OK);
    }
}
