package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.application.usecases.CustomerUseCase;
import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    @Autowired
    public CustomerController(CustomerUseCase customerUseCase) {
        this.customerUseCase = customerUseCase;
    }


    @GetMapping("/getAll")
    public ResponseEntity<Page<CustomerResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int elements
    ) {
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").ascending());
        return new ResponseEntity<>(this.customerUseCase.getAll(pageable), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Long customerId){
        return new ResponseEntity<>(this.customerUseCase.delete(customerId), HttpStatus.OK);
    }
}
