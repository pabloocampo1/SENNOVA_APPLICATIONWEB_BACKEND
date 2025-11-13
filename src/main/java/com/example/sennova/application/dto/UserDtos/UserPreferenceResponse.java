package com.example.sennova.application.dto.UserDtos;

public record UserPreferenceResponse(
        boolean inventoryEquipment,
        boolean inventoryReagents,
        boolean quotations,
        boolean results
) {
}
