package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.MissionRequest;
import com.bbm.employeeservice.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
