package com.example.sennova.infrastructure.adpaters;


import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.CustomerMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.CustomersRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomersAdapterImpl implements CustomerPersistencePort {

    private final CustomersRepositoryJpa customersRepositoryJpa;
    private final CustomerMapperDbo customerMapperDbo;

    @Autowired
    public CustomersAdapterImpl(CustomersRepositoryJpa customersRepositoryJpa, CustomerMapperDbo customerMapperDbo) {
        this.customersRepositoryJpa = customersRepositoryJpa;
        this.customerMapperDbo = customerMapperDbo;
    }

    public Page<CustomerResponseDto> getAll(Pageable pageable){

        return this.customersRepositoryJpa.getAll(pageable).map(
                c -> new CustomerResponseDto(
                        c.getCustomerId(),
                        c.getCustomerName(),
                        c.getEmail(),
                        c.getPhoneNumber(),
                        c.getCity(),
                        c.getAddress(),
                        c.getCreateAt(),
                        c.getTestRequestId(),
                        c.getRequestCode(),
                        c.getState(),
                        c.getPrice()
                ));
    }

    @Override
    public CustomerModel findBYEmail(String email) {
        return null;
    }

    @Override
    public CustomerModel save(CustomerModel customerModel) {
        CustomerEntity customer = this.customersRepositoryJpa.save(this.customerMapperDbo.toEntity(customerModel));

        return this.customerMapperDbo.toModel(
                customer
        );
    }

    @Override
    public List<TestRequestEntity> findAllTestRequestByCustomerId(Long customerId) {
        return this.customersRepositoryJpa.findAllTestRequestByCustomerId(customerId);
    }

    @Override
    public Void delete(Long customerId) {
        this.customersRepositoryJpa.deleteById(customerId);
        return null;
    }

    @Override
    public boolean existById(Long customerId) {
        return this.customersRepositoryJpa.existsById(customerId);
    }
}
