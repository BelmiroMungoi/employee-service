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
public class EmployeeResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate birthdate;
    private AddressResponse address;
    private Role role;
}
