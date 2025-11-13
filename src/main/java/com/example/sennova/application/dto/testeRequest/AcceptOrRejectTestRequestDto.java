package com.example.sennova.application.dto.testeRequest;

import jakarta.validation.constraints.Email;

public record AcceptOrRejectTestRequestDto(
         String emailCustomer,
        Boolean isApproved,
        String message,
        Long testRequestId
) {
}
