package com.example.sennova.application.dto.inventory;

import java.time.LocalDate;

public record UsageResponseDto(
        Long equipmentUsageId,
        String usageName,
        LocalDate createAt,
        LocalDate updateAt
) {
}
