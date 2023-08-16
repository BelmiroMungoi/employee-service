package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.*;
import com.bbm.employeeservice.model.dto.*;
import com.bbm.employeeservice.repository.DepartmentRepository;
import com.bbm.employeeservice.repository.EmployeeRepository;
import com.bbm.employeeservice.repository.EmployeeSearchDao;
import com.bbm.employeeservice.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeSearchDao employeeSearch;
    private final DepartmentService departmentService;
    private final AddressService addressService;
    private final MissionService missionService;

    public AppResponse createEmployee(EmployeeRequest employeeRequest, Long userId) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new BusinessException("Já existe um funcionário registrado com esse Email");
        }
        User user = new User(userId);
        Address saveAddress = addressService.saveAddress(employeeRequest);
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment(), userId);
        getDepartment.setEmployeeQuantity(getDepartment.getEmployeeQuantity() + 1);

        Employee employee = Employee.builder()
                .employeeIdentifier(UUID.randomUUID().toString())
                .firstname(employeeRequest.getFirstname())
                .lastname(employeeRequest.getLastname())
                .email(employeeRequest.getEmail())
                .birthdate(employeeRequest.getBirthdate())
                .address(saveAddress)
                .department(getDepartment)
                .role(Role.USER)
                .user(user)
                .build();
        employeeRepository.save(employee);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Funcionário foi Salvo com Sucesso")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    public Page<EmployeeResponse> getEmployees(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByUserId(pageRequest, userId);

        return employees.map(this::mapToEmployeeResponse);
    }

    public Page<EmployeeResponse> getEmployeesPerPage(int page, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByUserId(pageRequest, userId);

        return employees.map(this::mapToEmployeeResponse);
    }

    public Employee getEmployeeById(Long employeeId, Long userId) {
        return employeeRepository.findByIdAndUserId(employeeId, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com ID: " + employeeId + " não foi encontrado."));
    }

    public Page<EmployeeResponse> getEmployeeByFirstname(String firstname, Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByFirstnameContainsIgnoreCaseAndUserId(pageRequest, firstname, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com o nome: " + firstname + " não foi encontrado"));
        return employees.map(this::mapToEmployeeResponse);
    }

    public Page<EmployeeResponse> getEmployeeByFirstnamePerPage(int page, String firstname, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByFirstnameContainsIgnoreCaseAndUserId(pageRequest, firstname, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com o nome: " + firstname + " não foi encontrado"));
        return employees.map(this::mapToEmployeeResponse);
    }

    public List<EmployeeResponse> getAllEmployeesByDepartment(String departmentName, Long userId) {
        List<Employee> employees = employeeRepository.findAllByDepartmentNameAndUserId(departmentName, userId);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public AppResponse updateEmployee(Long employeeId, EmployeeRequest employeeRequest, Long userId) {
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment(), userId);
        getDepartment.setEmployeeQuantity(getDepartment.getEmployeeQuantity() + 1);
        Set<Mission> getMission = missionService.getMissionByName(employeeRequest.getMission(), userId);

        Employee employee = getEmployeeById(employeeId, userId);
        employee.getDepartment().setEmployeeQuantity(employee.getDepartment().getEmployeeQuantity() - 1);
        departmentRepository.save(employee.getDepartment());
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

    public void deleteEmployee(Long employeeId, Long userId) {
        Employee employee = getEmployeeById(employeeId, userId);
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

    public Integer getEmployeeQuantityByUser(Long userId) {
        return employeeRepository.countAllByUserId(userId);
    }

    public List<PositionResponse> getAllPosition() {
        List<Position> position = positionRepository.findAll();
        return position.stream().map(this::mapToPositionResponse).toList();
    }

    public EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeIdentifier(employee.getEmployeeIdentifier())
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .birthdate(employee.getBirthdate())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .department(DepartmentResponse.builder()
                        .id(employee.getDepartment().getId())
                        .name(employee.getDepartment().getName())
                        .shortName(employee.getDepartment().getShortName())
                        .build())
                .positionResponse(PositionResponse.builder()
                        .positionName(employee.getPosition().getPositionName())
                        .build())
                .address(AddressResponse.builder()
                        .houseNumber(employee.getAddress().getHouseNumber())
                        .street(employee.getAddress().getStreet())
                        .zipCode(employee.getAddress().getZipCode())
                        .build())
                .build();
    }

    public PositionResponse mapToPositionResponse(Position position) {
        return PositionResponse.builder()
                .positionName(position.getPositionName())
                .build();
    }
}
