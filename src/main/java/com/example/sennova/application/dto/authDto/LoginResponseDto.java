package com.example.sennova.application.dto.authDto;

import com.example.sennova.application.dto.UserDtos.UserPreferenceResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoginResponseDto(
        String accessToken,
        Long userId,
        Boolean status,
        String message,
        String position,
        String imageProfile,
        LocalDate timestamp,
        String authorities,
        Boolean userExist,
        Boolean available,
        String username,
        String name,
        UserPreferenceResponse userPreferenceResponse,
        String email,
        LocalDateTime lastSession
) {
}
