package com.bbm.employeeservice.model.dto;

import com.bbm.employeeservice.model.MissionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequest {

    @NotBlank
    private String missionName;
    @DateTimeFormat(iso = DATE, pattern = "dd/MM/yyyy")
    private LocalDateTime finishedDate;
    private MissionStatus missionStatus;
    private String firstname;
}
