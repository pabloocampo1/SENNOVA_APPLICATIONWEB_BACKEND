package com.example.sennova.application.dto.testeRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequestRecord(
        @NotBlank
         String customerName,

        @NotBlank
        String email,

        @NotNull
        Long phoneNumber,

        @NotBlank
        String address,

        @NotBlank
        String city

) {
}
