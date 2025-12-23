package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerUseCase {

    CustomerModel findOrCreateCustomer(CustomerModel customerModel);
    Page<CustomerResponseDto> getAll(Pageable pageable);
    CustomerModel save(CustomerModel customerModel);
    Void delete(Long customerId);
}
