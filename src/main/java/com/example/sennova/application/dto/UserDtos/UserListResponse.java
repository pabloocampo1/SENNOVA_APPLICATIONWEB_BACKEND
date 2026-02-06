package com.example.sennova.application.dto.UserDtos;

import com.example.sennova.domain.model.RoleModel;
import lombok.Data;

import java.time.LocalDate;


public record UserListResponse (
        Long userId,
        String name,
        boolean available,
        Long phoneNumber,
        String email,
        Long dni,
        String position,
        String imageProfile,
       String role,
        LocalDate createAt
){
}
