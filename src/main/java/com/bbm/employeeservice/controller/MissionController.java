package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.model.dto.MissionResponse;
import com.bbm.employeeservice.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mission")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createMission(@RequestBody MissionRequest request) {
        var mission = missionService.createMission(request);
        return new ResponseEntity<>(mission, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<MissionResponse>> getAllMission() {
        List<MissionResponse> missions = missionService.getAllMission();
        return new ResponseEntity<>(missions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateMission(@PathVariable Long id, @RequestBody MissionRequest request) {
        var mission = missionService.updateMission(id, request);
        return new ResponseEntity<>(mission, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return new ResponseEntity<>("Missão com o ID: " + id + " foi deletada", HttpStatus.OK);
    }
}
