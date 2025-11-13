package com.example.sennova.application.dto.UserDtos;

import java.time.LocalDate;


public record UserResponse(
        Long userId,
        String name,
        String username,
        Long dni,
        boolean available,
        Long phoneNumber,
        String email,
         String position,
        String imageProfile,
        String roleName,
        LocalDate createAt
) {}
