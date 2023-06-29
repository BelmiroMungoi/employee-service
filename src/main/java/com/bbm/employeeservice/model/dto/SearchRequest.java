package com.bbm.employeeservice.model.dto;

import lombok.Data;

@Data
public class SearchRequest {

    private String firstname;
    private String lastname;
    private String email;
}
