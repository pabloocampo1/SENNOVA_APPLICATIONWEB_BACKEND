package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.application.usecases.CustomerUseCase;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Page<CustomerResponseDto> getAll(Pageable pageable) {
        
        return this.customerPersistencePort.getAll(pageable);
    }

    @Override
    public CustomerModel save(CustomerModel customerModel) {
        return this.customerPersistencePort.save(customerModel);
    }

    @Override
    @Transactional
    public Void delete(@Valid Long customerId) {
        
        if (!this.customerPersistencePort.existById(customerId)) throw new   IllegalArgumentException("No puedes eliminar a un usuairo que no existe");

        // first check if the customer doesn't have test request
        List<TestRequestEntity> testRequestEntities = this.customerPersistencePort.findAllTestRequestByCustomerId(customerId);
        if(!testRequestEntities.isEmpty()){
            throw new IllegalArgumentException("No puedes eliminar un cliente con un ensayo");
        }
        this.customerPersistencePort.delete(customerId);
          return null;
    }
}
