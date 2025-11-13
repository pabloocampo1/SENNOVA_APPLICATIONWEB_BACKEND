package com.example.sennova.application.dto.testeRequest;

public record CustomerResponse(
        Long customerId,

        String customerName,

        String dni,

        String email,

        Long phoneNumber,

        String address,

        String city
) {
}
