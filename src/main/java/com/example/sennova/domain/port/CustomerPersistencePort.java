package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;

import java.util.List;

public interface CustomerPersistencePort {

    List<CustomerEntity> getAll();
    CustomerModel findBYEmail(String email);
    CustomerModel save(CustomerModel customerModel);
}
