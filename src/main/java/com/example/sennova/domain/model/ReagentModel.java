package com.example.sennova.domain.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReagentModel {

    private Long reagentsId;

    @NotBlank(message = "El nombre del reactivo es obligatorio.")
    private String reagentName;

    private String brand;

    private String purity;

    @NotNull(message = "Las unidades no pueden ser nulas.")
    @Min(value = 1, message = "Las unidades deben ser al menos 1.")
    private Integer units;

    @NotNull(message = "La cantidad no puede ser nula.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private double quantity;

    @NotBlank(message = "La unidad de medida es obligatoria.")
    private String unitOfMeasure;

    private String stateExpiration;

    private String state;

    private String responsible;

    private String senaInventoryTag;

    private String imageUrl;

    private Boolean isPresent;

    private String batch;

    @NotNull(message = "La fecha de expiración es obligatoria.")
    private LocalDate expirationDate;

    private LocalDate createAt;

    private LocalDate updateAt;


    private LocationModel location;

    private UsageModel usage;

}
