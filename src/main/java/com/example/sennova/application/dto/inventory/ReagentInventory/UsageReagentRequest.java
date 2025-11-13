package com.example.sennova.application.dto.inventory.ReagentInventory;

import jakarta.validation.constraints.NotNull;

public record UsageReagentRequest(
        String responsibleName,
        Long reagentId,
        @NotNull(message = "No se puede guardar un registro sin la cantidad")
        Double quantity,
        String notes
) {
}
