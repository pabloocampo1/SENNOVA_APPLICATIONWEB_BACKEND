package com.example.sennova.application.dto.inventory.ReagentInventory;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReagentRequestDto(

        Long reagentId,

        @NotBlank(message = "El nombre del reactivo es obligatorio.")
        @Size(max = 100, message = "El nombre del reactivo no puede superar los 100 caracteres.")
        String reagentName,

        @Size(max = 100, message = "La marca no puede superar los 100 caracteres.")
        String brand,

        @Size(max = 100, message = "La pureza no puede superar los 100 caracteres.")
        String purity,

        @NotNull(message = "El número de unidades es obligatorio.")
        @Min(value = 1, message = "Las unidades deben ser al menos 1.")
        Integer units,

        @NotNull(message = "La cantidad es obligatoria.")
        @Min(value = 1, message = "La cantidad debe ser al menos 1.")
        Double quantity,

        @NotBlank(message = "La unidad de medida es obligatoria.")
        String unitOfMeasure,

        @Size(max = 50, message = "El lote no puede superar los 50 caracteres.")
        String batch,

        @NotNull(message = "La fecha de expiración es obligatoria.")
        LocalDate expirationDate,

        @NotNull(message = "El usuario es obligatorio.")
        Long responsibleId,

        @NotNull(message = "El uso es obligatorio.")
        Long usageId,

        String senaInventoryTag,

        Boolean isPresent,

        @NotNull(message = "La ubicacion  es obligatorio.")
        Long locationId
) {}
