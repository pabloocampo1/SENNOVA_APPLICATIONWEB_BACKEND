package com.example.sennova.application.dto.authDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Debes de ingresar tu nombre de usuario") String username,
        @NotBlank(message = "Debes de ingresar la contrseña") String password
) {
}
