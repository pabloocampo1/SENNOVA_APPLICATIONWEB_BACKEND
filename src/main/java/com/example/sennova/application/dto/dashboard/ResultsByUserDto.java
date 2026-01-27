package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class ResultsByUserDto {
    private String name;
    private Long total;
    private Integer year;
    private Integer month;
}
