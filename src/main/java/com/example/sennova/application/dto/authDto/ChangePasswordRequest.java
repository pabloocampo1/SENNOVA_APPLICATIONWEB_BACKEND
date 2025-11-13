package com.example.sennova.application.dto.authDto;

import jakarta.validation.Valid;

public record ChangePasswordRequest(
       @Valid String password,
       @Valid String newPassword
) {
}
