package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.*;
import com.bbm.employeeservice.model.dto.*;
import com.bbm.employeeservice.repository.EmployeeRepository;
import com.bbm.employeeservice.repository.EmployeeSearchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSearchDao employeeSearch;
    private final DepartmentService departmentService;
    private final AddressService addressService;
    private final MissionService missionService;

    public AppResponse createEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new BusinessException("Já existe um funcionário registrado com esse Email");
        }
        Address saveAddress = addressService.saveAddress(employeeRequest);
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment());

        Employee employee = Employee.builder()
                .employeeIdentifier(UUID.randomUUID().toString())
                .firstname(employeeRequest.getFirstname())
                .lastname(employeeRequest.getLastname())
                .email(employeeRequest.getEmail())
                .birthdate(employeeRequest.getBirthdate())
                .address(saveAddress)
                .department(getDepartment)
                .role(Role.USER)
                .build();
        employeeRepository.save(employee);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Funcionário foi Salvo com Sucesso")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com ID: " + employeeId + " não foi encontrado."));
    }

    public Set<Employee> getEmployeeByFirstname(String firstname) {
        return employeeRepository.findAllByFirstname(firstname).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com o nome: " + firstname + " não foi encontrado"));
    }

    public AppResponse updateEmployee(Long employeeId, EmployeeRequest employeeRequest) {
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment());
        Set<Mission> getMission = missionService.getMissionByName(employeeRequest.getMission());

        Employee employee = getEmployeeById(employeeId);
        Address updateAddress = addressService.updateAddress(employee.getAddress().getId(), employeeRequest);
        employee.setFirstname(employeeRequest.getFirstname());
        employee.setLastname(employeeRequest.getLastname());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBirthdate(employeeRequest.getBirthdate());
        employee.setAddress(updateAddress);
        employee.setDepartment(getDepartment);
        employee.setMissions(getMission);
        employeeRepository.save(employee);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Funcionário foi actualizado com sucesso")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
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
                .department(DepartmentResponse.builder()
                        .id(employee.getDepartment().getId())
                        .name(employee.getDepartment().getName())
                        .build())
                .role(employee.getRole())
                .build();
    }
}
