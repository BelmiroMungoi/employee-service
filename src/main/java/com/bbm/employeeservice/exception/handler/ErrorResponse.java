package com.bbm.employeeservice.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private String status;
    private String title;
    private OffsetDateTime time;
    private List<Campo> campos;

    @Data
    @AllArgsConstructor
    public static class Campo {

        private String name;
        private String message;
    }
}
