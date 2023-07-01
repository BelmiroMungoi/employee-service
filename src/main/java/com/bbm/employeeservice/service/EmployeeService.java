package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Address;
import com.bbm.employeeservice.model.Employee;
import com.bbm.employeeservice.model.Role;
import com.bbm.employeeservice.model.dto.AddressResponse;
import com.bbm.employeeservice.model.dto.EmployeeRequest;
import com.bbm.employeeservice.model.dto.EmployeeResponse;
import com.bbm.employeeservice.model.dto.SearchRequest;
import com.bbm.employeeservice.repository.EmployeeRepository;
import com.bbm.employeeservice.repository.EmployeeSearchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSearchDao employeeSearch;
    private final AddressService addressService;

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new BusinessException("Já existe um funcionário registrado com esse Email");
        }
        Address saveAddress = addressService.saveAddress(employeeRequest);

        Employee employee = Employee.builder()
                .employeeIdentifier(UUID.randomUUID().toString())
                .firstname(employeeRequest.getFirstname())
                .lastname(employeeRequest.getLastname())
                .email(employeeRequest.getEmail())
                .birthdate(employeeRequest.getBirthdate())
                .address(saveAddress)
                .role(Role.USER)
                .build();

        return employeeRepository.save(employee);
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com ID: " + employeeId + " não foi encontrado."));
    }

    public Employee updateEmployee(Long employeeId, EmployeeRequest employeeRequest) {
        Address saveAddress = addressService.saveAddress(employeeRequest);

        Employee employee = getEmployeeById(employeeId);
        employee.setFirstname(employeeRequest.getFirstname());
        employee.setLastname(employeeRequest.getLastname());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBirthdate(employeeRequest.getBirthdate());
        employee.setAddress(saveAddress);

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employeeRepository.delete(employee);
    }

    public List<EmployeeResponse> searchAllEmployees(String firstname, String lastname, String email) {
        List<Employee> employees = employeeSearch.findAllBySimpleQuery(firstname, lastname, email);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public List<EmployeeResponse> searchAllEmployeesByName(SearchRequest request) {
        List<Employee> employees = employeeSearch.findAllByCriteria(request);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .birthdate(employee.getBirthdate())
                .address(AddressResponse.builder()
                        .houseNumber(employee.getAddress().getHouseNumber())
                        .street(employee.getAddress().getStreet())
                        .zipCode(employee.getAddress().getZipCode())
                        .build())
                .role(employee.getRole())
                .build();
    }
}
