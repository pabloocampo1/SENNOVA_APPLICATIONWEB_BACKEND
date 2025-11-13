package com.example.sennova.infrastructure.adpaters;


import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.CustomerMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.CustomersRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<CustomerEntity> getAll(){
        return this.customersRepositoryJpa.findAll();
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
}
