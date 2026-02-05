package com.example.sennova.domain.port;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerPersistencePort {

    Page<CustomerResponseDto> getAll(Pageable pageable);
    CustomerModel findBYEmail(String email);
    CustomerModel save(CustomerModel customerModel);
    List<TestRequestEntity> findAllTestRequestByCustomerId(Long customerId);
     Void delete(Long customerId);
     boolean existById(Long customerId);
     Optional<CustomerModel> findById(Long customerId);
}
