package com.example.sennova.application.dto.inventory.EquipmentInventory.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MaintenanceEquipmentRequest(
        @NotBlank(message = "Debe contener el nombre de la persona")
        String performedBy,
        @Size(max = 400, message = "La nota no debe de contener mas de 400 caracteres")
        String notes,
        @NotNull(message = "Debes de agregar la fecha para el registro")
        LocalDate dateMaintenance,
        @NotNull(message = "El registro debe de contener el id del equipo, intentalo de nuevo")
        Long equipmentId,
        @NotBlank(message = "Debe contener un tipo de mantenimiento")
        String maintenanceType
) {
}
