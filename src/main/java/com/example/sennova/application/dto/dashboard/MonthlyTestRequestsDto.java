package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class MonthlyTestRequestsDto {
    private String period;
    private Integer total;

    public MonthlyTestRequestsDto(String period, Integer total) {
        this.period = period;
        this.total = total;
    }
}
