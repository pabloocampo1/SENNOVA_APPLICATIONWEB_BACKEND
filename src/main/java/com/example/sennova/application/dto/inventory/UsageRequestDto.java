package com.example.sennova.application.dto.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsageRequestDto(

        Long equipmentUsageId,

        @NotBlank(message = "El nombre de uso no puede estar vacío")
        @Size(max = 255, message = "El nombre de uso no puede tener más de 255 caracteres")
        String usageName
) {
}
