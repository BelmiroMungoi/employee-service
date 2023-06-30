package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Employee;
import com.bbm.employeeservice.model.dto.EmployeeRequest;
import com.bbm.employeeservice.model.dto.EmployeeResponse;
import com.bbm.employeeservice.model.dto.SearchRequest;
import com.bbm.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.createEmployee(employeeRequest);
        var employeeResponse = employeeService.mapToEmployeeResponse(employee);

        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getEmployees();
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
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @RequestBody EmployeeRequest employeeRequest, @PathVariable Long id) {
        Employee employee = employeeService.updateEmployee(id, employeeRequest);
        var employeeResponse = employeeService.mapToEmployeeResponse(employee);

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Funcionário com ID: " + id + " foi Deletado com Sucesso");
    }
}
