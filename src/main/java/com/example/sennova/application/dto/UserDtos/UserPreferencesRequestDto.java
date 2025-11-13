package com.example.sennova.application.dto.UserDtos;

public record UserPreferencesRequestDto(
        boolean inventoryEquipment,
        boolean inventoryReagents,
        boolean quotations,
        boolean results
) {
}
