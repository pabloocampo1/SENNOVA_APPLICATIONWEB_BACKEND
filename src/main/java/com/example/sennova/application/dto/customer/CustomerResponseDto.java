package com.example.sennova.application.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long customerId;
    private String customerName;
    private String email;
    private Long phoneNumber;
    private String city;
    private String dni;
    private String address;
    private LocalDate createAt;

    private Long testRequestId;
    private String requestCode;
    private String state;
    private Double price;
}
