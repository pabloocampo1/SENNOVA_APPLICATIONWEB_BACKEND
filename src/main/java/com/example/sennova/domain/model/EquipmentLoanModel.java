package com.example.sennova.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EquipmentLoanModel {
    private Long equipmentLoanId;

    @Size(message = "La descripción es demasiado larga", max = 400)
    private String notes;

    private LocalDate loanDate;

    @NotBlank(message = "El prestamo debe de incluir el nombre de la persona.")
    private String nameLoan;

    @NotBlank(message = "El registro debe de contar con un tipo.")
    private String type;

    @NotNull(message = "debe de contener un equipo para registrar el prestamo")
    @JsonIgnore
    private EquipmentModel equipment;

    private LocalDate createAt;

    private LocalDate updateAt;


}
