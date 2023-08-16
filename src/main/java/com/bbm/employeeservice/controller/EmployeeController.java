package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Employee;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.*;
import com.bbm.employeeservice.service.EmployeeService;
import com.bbm.employeeservice.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final ReportService reportService;
    private final EmployeeService employeeService;

    @PostMapping("/")
    public ResponseEntity<AppResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest,
                                                      @AuthenticationPrincipal User authenticatedUser) {
        var employee = employeeService.createEmployee(employeeRequest, authenticatedUser.getId());
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(@AuthenticationPrincipal User authenticatedUser) {
        Page<EmployeeResponse> employees = employeeService.getEmployees(authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployeesPerPage(@PathVariable("page") int page,
                                                                             @AuthenticationPrincipal User authenticatedUser) {
        Page<EmployeeResponse> employees = employeeService.getEmployeesPerPage(page, authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{department}")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeeByDepartment(@PathVariable("department") String department,
                                                                             @AuthenticationPrincipal User authenticatedUser) {
        List<EmployeeResponse> employees = employeeService.
                getAllEmployeesByDepartment(department, authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("id") Long id, @AuthenticationPrincipal User authenticatedUser) {
        Employee employee = employeeService.getEmployeeById(id, authenticatedUser.getId());
        return ResponseEntity.ok(employeeService.mapToEmployeeResponse(employee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> searchAllEmployees(
            @RequestParam String firstname, @RequestParam String lastname, @RequestParam String email) {
        List<EmployeeResponse> employees = employeeService.searchAllEmployees(firstname, lastname, email);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployeeByFirstname(@PathVariable("name") String name,
                                                                           @AuthenticationPrincipal User authenticatedUser) {
        Page<EmployeeResponse> employees = employeeService
                .getEmployeeByFirstname(name, authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/name/{name}/page/{page}")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployeeByFirstnamePerPage(@PathVariable("name") String name,
                                                                            @PathVariable("page") int page,
                                                                            @AuthenticationPrincipal User authenticatedUser) {
        Page<EmployeeResponse> employees = employeeService
                .getEmployeeByFirstnamePerPage(page, name, authenticatedUser.getId());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> getEmployeeQuantityByUser(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(employeeService.getEmployeeQuantityByUser(authenticatedUser.getId()));
    }

    @GetMapping("/position")
    public ResponseEntity<List<PositionResponse>> getAllPosition() {
        return ResponseEntity.ok(employeeService.getAllPosition());
    }

    @PostMapping("/search")
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
        return ResponseEntity.ok("Funcionário com ID: " + id + " foi Eliminado com Sucesso");
    }

    @GetMapping("/report")
    public ResponseEntity<String> downloadReport(HttpServletResponse response) throws IOException {
        byte[] pdf = reportService.generateReport(new HashMap<>());
        String base64 = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
       /*response.setContentLength(pdf.length);
        response.setContentType("application/octet-stream");
        String header = "Content-Disposition";
        String headerValue = String.format("attachment; filename\"%s\"", "Relatório de Funcionários.pdf");
        response.setHeader(header, headerValue);
        response.getOutputStream().write(pdf);*/
        return new ResponseEntity<>(base64, HttpStatus.OK);
    }
}
