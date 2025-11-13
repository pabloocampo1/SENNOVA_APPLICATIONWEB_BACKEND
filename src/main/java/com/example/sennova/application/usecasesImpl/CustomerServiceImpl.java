package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.CustomerUseCase;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.port.CustomerPersistencePort;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerUseCase {

    private final CustomerPersistencePort customerPersistencePort;

    public CustomerServiceImpl(CustomerPersistencePort customerPersistencePort) {
        this.customerPersistencePort = customerPersistencePort;
    }

    @Override
    public CustomerModel findOrCreateCustomer(CustomerModel customerModel) {

        CustomerModel existCustomer =  this.customerPersistencePort.findBYEmail(customerModel.getEmail());

        if(existCustomer != null){
            return existCustomer;
        }

        return this.customerPersistencePort.save(customerModel);
    }

    @Override
    public CustomerModel save(CustomerModel customerModel) {
        return this.customerPersistencePort.save(customerModel);
    }
}
