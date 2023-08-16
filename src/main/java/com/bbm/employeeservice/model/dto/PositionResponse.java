package com.bbm.employeeservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionResponse{

    private String positionName;
}
