package com.example.sennova.application.dto.customer;

public record CustomerEditDto(
        String name,
        Long phoneNumber,
        Long dni,
        String email,
        String city,
        String address
) {
}
