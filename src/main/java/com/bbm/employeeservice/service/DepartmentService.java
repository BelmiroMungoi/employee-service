package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Department;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.DepartmentRequest;
import com.bbm.employeeservice.model.dto.DepartmentResponse;
import com.bbm.employeeservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public AppResponse createDepartment(DepartmentRequest request) {
        Department department = Department.builder()
                .name(request.getName())
                .build();
        departmentRepository.save(department);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Departamento foi Salvo com Sucesso")
                .name(department.getName())
                .build();
    }

    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public List<DepartmentResponse> getAllDepartmentByName(String name) {
        List<Department> departments = departmentRepository.findAllByNameLikeIgnoreCase(name);
        return departments.stream().map(this::mapToDepartmentResponse).toList();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Departamento com o ID: " + id + " não foi encontrado"));
    }

    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(this::mapToDepartmentResponse).toList();
    }

    public AppResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = getDepartmentById(id);
        department.setName(request.getName());
        departmentRepository.save(department);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Departamento foi Actualizado com Sucesso")
                .name(department.getName())
                .build();
    }

    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

}
