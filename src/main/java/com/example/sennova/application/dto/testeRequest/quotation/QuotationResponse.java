package com.example.sennova.application.dto.testeRequest.quotation;

import com.example.sennova.domain.model.testRequest.CustomerModel;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;

@Data
public class QuotationResponse {
    private Long testRequestId;
    private String requestCode;
    private String state;
    private Double price;
    private LocalDate createAt;
    private Boolean isApproved;
    private CustomerModel customer;
    private LocalDate approvalDate;
    private LocalDate discardDate;
    private List<SampleQuotationResponse> samples;
}
