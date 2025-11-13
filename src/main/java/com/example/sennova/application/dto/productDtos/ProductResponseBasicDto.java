package com.example.sennova.application.dto.productDtos;


import java.time.LocalDate;

public record ProductResponseBasicDto(
        Long productId,
        String analysis,
        String matrix,
        String method,
        String equipment,
        String units,
        double price,
        String notes,
        LocalDate createAt,
        LocalDate updateAt
) {}
