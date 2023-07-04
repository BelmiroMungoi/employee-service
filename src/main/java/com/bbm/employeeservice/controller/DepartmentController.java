package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.DepartmentRequest;
import com.bbm.employeeservice.model.dto.DepartmentResponse;
import com.bbm.employeeservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createDepartment(@RequestBody DepartmentRequest request) {
        var department = departmentService.createDepartment(request);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartment() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequest request) {
        var department = departmentService.updateDepartment(id, request);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>("Departamento com o ID: " + id + " foi deletado com SUCESSO", HttpStatus.OK);
    }
}