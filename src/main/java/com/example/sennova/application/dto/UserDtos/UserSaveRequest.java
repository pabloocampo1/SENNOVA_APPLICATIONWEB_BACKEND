package com.example.sennova.application.dto.UserDtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserSaveRequest(


        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "El DNI es obligatorio")
        @Min(value = 1000000, message = "DNI no válido")
        Long dni,

        Boolean available,


        @NotNull(message = "El número de teléfono es obligatorio")
        @Digits(integer = 10, fraction = 0, message = "Número de teléfono inválido")
        Long phoneNumber,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo electrónico inválido")
        String email,

        String position,

        String imageProfile,


        @NotBlank(message = "El usuario no puede ser guardado sin especificar su rol.")
        String roleName

) {}