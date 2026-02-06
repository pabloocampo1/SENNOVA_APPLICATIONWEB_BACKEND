package com.example.sennova.application.dto.testeRequest;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

public record SampleAnalysisRequestRecord(
        @NotNull(message = "El ID del análisis no puede ser nulo")
        Long sampleProductAnalysisId,

        @NotBlank(message = "El resultado final es obligatorio")
        @Size(max = 255, message = "El resultado final no debe exceder 255 caracteres")
        String resultFinal,

        @NotNull(message = "La fecha del resultado es obligatoria")
        @PastOrPresent(message = "La fecha debe ser pasada o actual")
        LocalDate resultDate,

        @NotBlank(message = "La unidad es obligatoria")
        @Size(max = 50, message = "La unidad no debe exceder 50 caracteres")
        String unit,

        @NotBlank(message = "El estado de aprobación es obligatorio")
        String passStatus,

        @NotBlank(message = "El estado de acreditación es obligatorio")
        String accreditationStatus,

        @NotBlank(message = "Los estándares son obligatorios")
        String standards,
        

        @Size(max = 500, message = "Las notas no deben exceder 500 caracteres")
        String notes ,

        String incert,

        String valueRef,


        String resultGeneratedBy

) {
}
