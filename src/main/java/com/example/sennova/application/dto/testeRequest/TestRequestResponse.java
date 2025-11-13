package com.example.sennova.application.dto.testeRequest;


import java.time.LocalDate;

public record TestRequestResponse(
        Long testRequestId,
        String requestCode,
        LocalDate testDate,
        LocalDate approvalDate,
        LocalDate discardDate,
        double price,
        Boolean isApproved,
        LocalDate createAt,
        LocalDate updateAt,
        String notes
) {
}
