package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Department;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.DepartmentRequest;
import com.bbm.employeeservice.model.dto.DepartmentResponse;
import com.bbm.employeeservice.service.DepartmentService;
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
    @CacheEvict(value = "departments", allEntries = true)
    @CachePut("departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartment(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getAllDepartments(authenticatedUser.getId()));
    }
    @GetMapping("/page/{page}")
    @CacheEvict(value = "departments", allEntries = true)
    @CachePut("departments")
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartment(@PathVariable("page") int page,
                                                                     @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getAllDepartments(page, authenticatedUser.getId()));
    }

    @GetMapping("/{name}/page/{page}")
    @CacheEvict(value = "departments", allEntries = true)
    @CachePut("departments")
    public ResponseEntity<Page<DepartmentResponse>> getDepartmentByName(@PathVariable("name") String name,
                                                                        @PathVariable("page") int page,
                                                                        @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getAllDepartmentByName(page, name, authenticatedUser.getId()));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("id") Long id,
                                                                @AuthenticationPrincipal User authenticatedUser){
        Department department = departmentService.getDepartmentById(id, authenticatedUser.getId());
        return ResponseEntity.ok(departmentService.mapToDepartmentResponse(department));
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> getDepartmentQuantityByUser(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(departmentService.getDepartmentQuantity(authenticatedUser.getId()));
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
