package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.application.dto.testeRequest.CustomerRequestRecord;
import com.example.sennova.application.dto.testeRequest.CustomerResponse;
import com.example.sennova.application.mapper.CustomerMapper;
import com.example.sennova.application.usecases.CustomerUseCase;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.example.sennova.web.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerUseCase {

    private final CustomerPersistencePort customerPersistencePort;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerPersistencePort customerPersistencePort, CustomerMapper customerMapper) {
        this.customerPersistencePort = customerPersistencePort;
        this.customerMapper = customerMapper;
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

    @Override
    public CustomerResponse update(Long customerId, CustomerRequestRecord dto) {
        CustomerModel customerModel = this.customerPersistencePort.findById(customerId).orElseThrow(() -> new EntityNotFoundException("No se encontro el cliente con id _  "+ customerId));
        customerModel.setCustomerName(dto.customerName());
        customerModel.setCity(dto.city());
        customerModel.setEmail(dto.email());
        customerModel.setDni(dto.dni());
        customerModel.setPhoneNumber(dto.phoneNumber());
        customerModel.setAddress(dto.address());
        CustomerModel customerUpdated = this.save(customerModel);
        return this.customerMapper.toResponse(customerUpdated);
    }
}
