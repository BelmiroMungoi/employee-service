package com.bbm.employeeservice.model.dto;

import com.bbm.employeeservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    private String employeeIdentifier;
    private String firstname;
    private String lastname;
    private String email;
    private String houseNumber;
    private String street;
    private String zipCode;
    private LocalDate birthdate;
    private Role role;
}
