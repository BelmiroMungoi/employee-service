package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.model.dto.MissionResponse;
import com.bbm.employeeservice.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/")
    public ResponseEntity<List<MissionResponse>> getAllMission(@AuthenticationPrincipal User authenticatedUser) {
        List<MissionResponse> missions = missionService.getAllMission(authenticatedUser.getId());
        return new ResponseEntity<>(missions, HttpStatus.OK);
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
