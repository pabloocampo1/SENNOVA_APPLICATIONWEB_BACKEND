package com.example.sennova.application.dto.inventory.EquipmentInventory.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EquipmentRequestDto(

        Long equipmentId,

        @NotBlank(message = "El código interno es obligatorio")
        @Size(max = 100, message = "El código interno no debe superar los 100 caracteres")
        String internalCode,

        @NotBlank(message = "El nombre del equipo es obligatorio")
        String equipmentName,

        boolean markReport,

        @Size(max = 100, message = "La marca no debe superar los 100 caracteres")
        String brand,

        @Size(max = 100, message = "El modelo no debe superar los 100 caracteres")
        String model,

        @NotNull(message = "El número de serie es obligatorio")
        @Positive(message = "El número de serie debe ser un valor positivo")
        String serialNumber,

        String senaInventoryTag,

        @PastOrPresent(message = "La fecha de adquisición no puede estar en el futuro")
        LocalDate acquisitionDate,

        @NotNull(message = "La fecha de mantenimiento es obligatoria")
        LocalDate maintenanceDate,

        @Size(max = 50, message = "El amperaje no debe superar los 50 caracteres")
        String amperage,

        @Size(max = 50, message = "El voltaje no debe superar los 50 caracteres")
        String voltage,

        @Positive(message = "El costo del equipo debe ser mayor a 0")
        double equipmentCost,

        @NotBlank(message = "El estado es obligatorio")
        String state,

        @NotNull(message = "El ID del responsable es obligatorio")
        String responsible,

        @NotNull(message = "El ID de la ubicación del equipo es obligatorio")
        Long locationId,

        @NotNull(message = "El ID del uso es obligatorio")
        Long usageId,

        String imageUrl,

        String description
) {}
