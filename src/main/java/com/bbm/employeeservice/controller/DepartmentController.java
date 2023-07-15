package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.DepartmentRequest;
import com.bbm.employeeservice.model.dto.DepartmentResponse;
import com.bbm.employeeservice.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createDepartment(@Valid @RequestBody DepartmentRequest request,
                                                        @AuthenticationPrincipal User authenticatedUser) {
        var department = departmentService.createDepartment(request, authenticatedUser.getId());
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartment(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getAllDepartments(authenticatedUser.getId()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<DepartmentResponse>> getDepartmentByName(@PathVariable("name") String name,
                                                                        @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getAllDepartmentByName(name, authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request,
                                                        @AuthenticationPrincipal User authenticatedUser) {
        var department = departmentService.updateDepartment(id, request, authenticatedUser.getId());
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        departmentService.deleteDepartment(id, authenticatedUser.getId());
        return new ResponseEntity<>("Departamento com o ID: " + id + " foi deletado com SUCESSO", HttpStatus.OK);
    }
}
