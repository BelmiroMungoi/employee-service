package com.bbm.employeeservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {

    private Long id;
    private String missionName;
    private int missionDuration;
}
