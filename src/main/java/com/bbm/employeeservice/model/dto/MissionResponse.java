package com.bbm.employeeservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {

    private Long id;
    private String missionName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime startedDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate finishedDate;
    private StatusResponse missionStatus;
}
