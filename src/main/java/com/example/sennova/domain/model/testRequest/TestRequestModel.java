package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestRequestModel {

    private Long testRequestId;

    private String requestCode;

    private LocalDate testDate;

    private LocalDate approvalDate;

    private LocalDate  dueDate;

    private LocalDate discardDate;

    private LocalDate submissionDate;

    private double price;

    private Boolean isFinished;

    private String deliveryStatus;

    private Boolean isApproved;
    
     private String state;
    private LocalDate createAt;

    private LocalDate updateAt;

    private String notes;

    private CustomerModel customer;

    private List<SampleModel> samples;

}
