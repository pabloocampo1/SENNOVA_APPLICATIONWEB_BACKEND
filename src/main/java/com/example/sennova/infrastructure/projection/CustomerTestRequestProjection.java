package com.example.sennova.infrastructure.projection;

import java.time.LocalDate;

public interface CustomerTestRequestProjection {
    Long getCustomerId();
    String getCustomerName();
    String getEmail();
    Long getPhoneNumber();
    String getCity();
    String getAddress();
    LocalDate getCreateAt();
    String getDni();
    Long getTestRequestId();
    String getRequestCode();
    String getState();
    Double getPrice();
}



