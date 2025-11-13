package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerModel {
    private Long customerId;

   
    private String customerName;

   
    private String dni;

   
    private String email;

  
    private Long phoneNumber;

   
    private String address;

   
    private String city;

    
    private LocalDate createAt;

    private LocalDate updateAt;
}
