package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.EmployeeRequest;
import com.bbm.employeeservice.model.dto.EmployeeResponse;
import com.bbm.employeeservice.model.dto.SearchRequest;
import com.bbm.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest,
                                                      @AuthenticationPrincipal User authenticatedUser) {
        var employee = employeeService.createEmployee(employeeRequest, authenticatedUser.getId());
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(@AuthenticationPrincipal User authenticatedUser) {
        List<EmployeeResponse> employees = employeeService.getEmployees(authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{department}")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeeByDepartment(@PathVariable("department") String department,
                                                                             @AuthenticationPrincipal User authenticatedUser) {
        List<EmployeeResponse> employees = employeeService.
                getAllEmployeesByDepartment(department, authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> searchAllEmployees(
            @RequestParam String firstname, @RequestParam String lastname, @RequestParam String email) {
        List<EmployeeResponse> employees = employeeService.searchAllEmployees(firstname, lastname, email);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchAllEmployeesByName(@RequestBody SearchRequest request) {
        List<EmployeeResponse> employees = employeeService.searchAllEmployeesByName(request);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateEmployee(@Valid @RequestBody EmployeeRequest employeeRequest,
                                                      @PathVariable Long id,
                                                      @AuthenticationPrincipal User authenticatedUser) {
        var employeeResponse = employeeService.updateEmployee(id, employeeRequest, authenticatedUser.getId());

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        employeeService.deleteEmployee(id, authenticatedUser.getId());
        return ResponseEntity.ok("Funcionário com ID: " + id + " foi Deletado com Sucesso");
    }
}
