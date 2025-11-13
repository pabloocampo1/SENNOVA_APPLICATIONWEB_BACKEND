package com.example.sennova.application.dto.inventory.EquipmentInventory.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipmentLoanRequest(
        @NotNull(message = "El equipo es obligatorio")
        Long equipmentId,

        @NotBlank(message = "El nombre del préstamo es obligatorio")
        @Size(max = 100, message = "El nombre del préstamo no puede superar los 100 caracteres")
        String nameLoan,

        @Size(max = 400, message = "La descripción es demasiado larga (máx 400 caracteres)")
        String notes,

        @NotNull(message = "La fecha del préstamo es obligatoria")
        LocalDate loanDate,

        @NotBlank(message = "El tipo de préstamo es obligatorio")
        String type
) {
}
