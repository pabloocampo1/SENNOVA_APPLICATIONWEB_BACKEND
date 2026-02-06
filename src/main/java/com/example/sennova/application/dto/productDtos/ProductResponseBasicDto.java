package com.example.sennova.application.dto.productDtos;


import java.time.LocalDate;

public record ProductResponseBasicDto(
        Long analysisId,
        String analysisName,
        String method,
        String equipment,
        boolean available,
        String units,
        double price,
        String notes,
        LocalDate createAt,
        LocalDate updateAt
) {}
