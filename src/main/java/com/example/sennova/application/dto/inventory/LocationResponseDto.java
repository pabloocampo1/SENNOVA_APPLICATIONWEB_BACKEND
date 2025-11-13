package com.example.sennova.application.dto.inventory;

import java.time.LocalDate;

public record LocationResponseDto(
        Long equipmentLocationId,
        String locationName,
        LocalDate createAt,
        LocalDate updateAt

) {
}
