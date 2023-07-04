package com.bbm.employeeservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequest {

    private String missionName;
    private int missionDuration;
    private String firstname;
}
