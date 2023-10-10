package com.bbm.employeeservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusChartResponse {

    private Integer open;
    private Integer pendent;
    private Integer canceled;
    private Integer closed;
}
