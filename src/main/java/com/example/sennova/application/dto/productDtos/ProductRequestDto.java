package com.example.sennova.application.dto.productDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProductRequestDto(
        Long analysisId,

        @NotBlank(message = "El análisis es obligatorio")
        String analysisName,

       

        @NotBlank(message = "El método es obligatorio")
        String method,

        @NotBlank(message = "El equipo es obligatorio")
        String equipment,

        @NotBlank(message = "Las unidades son obligatorias")
        String units,

        @Positive(message = "El precio debe ser mayor que 0")
        double price,

        @Size(max = 500, message = "Las notas no pueden superar los 500 caracteres")
        String notes

) {
}
