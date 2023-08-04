package com.bbm.employeeservice.model.dto;

import com.bbm.employeeservice.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String firstname;
    private String lastname;
    private String email;
    private boolean isEnabled;
    private Role role;
}
