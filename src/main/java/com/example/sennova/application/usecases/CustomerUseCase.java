package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.testRequest.CustomerModel;

public interface CustomerUseCase {

    CustomerModel findOrCreateCustomer(CustomerModel customerModel);

    CustomerModel save(CustomerModel customerModel);
}
