package com.bbm.employeeservice.model.dto;

import com.bbm.employeeservice.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String employeeIdentifier;
    private String firstname;
    private String lastname;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;
    private AddressResponse address;
    private DepartmentResponse department;
    private Role role;
    private Double salary;
    private PositionResponse positionResponse;
}
