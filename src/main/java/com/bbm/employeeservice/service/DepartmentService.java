package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Department;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.DepartmentRequest;
import com.bbm.employeeservice.model.dto.DepartmentResponse;
import com.bbm.employeeservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public AppResponse createDepartment(DepartmentRequest request, Long userId) {
        User user = new User(userId);
        Department department = Department.builder()
                .name(request.getName())
                .shortName(request.getShortName())
                .employeeQuantity(0)
                .user(user)
                .build();
        departmentRepository.save(department);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Departamento foi Salvo com Sucesso")
                .name(department.getName())
                .build();
    }

    public Department getDepartmentByName(String name, Long userId) {
        return departmentRepository.findByNameAndUserId(name, userId);
    }

    public Page<DepartmentResponse> getAllDepartmentByName(int page, String name, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Department> departments = departmentRepository.findAllByNameContainsIgnoreCaseAndUserId(pageRequest, name, userId);
        return departments.map(this::mapToDepartmentResponse);
    }

    public Department getDepartmentById(Long id, Long userId) {
        return departmentRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new EntityNotFoundException("Departamento com o ID: " + id + " não foi encontrado"));
    }

    public Page<DepartmentResponse> getAllDepartments(int page, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Department> departments = departmentRepository.findAllByUserId(pageRequest, userId);

        return departments.map(this::mapToDepartmentResponse);
    }

    public List<DepartmentResponse> getAllDepartments(Long userId) {
        List<Department> departments = departmentRepository.findAllByUserId(userId);

        return departments.stream().map(this::mapToDepartmentResponse).toList();
    }

    public AppResponse updateDepartment(Long id, DepartmentRequest request, Long userId) {
        Department department = getDepartmentById(id, userId);
        department.setName(request.getName());
        department.setShortName(request.getShortName());
        departmentRepository.save(department);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Departamento foi Actualizado com Sucesso")
                .name(department.getName())
                .build();
    }

    public void deleteDepartment(Long id, Long userId) {
        Department department = getDepartmentById(id, userId);
        departmentRepository.delete(department);
    }

    public DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .shortName(department.getShortName())
                .employeeQuantity(department.getEmployeeQuantity())
                .build();
    }

}
