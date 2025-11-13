package com.example.sennova.web.controllers;

import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerPersistencePort customerPersistencePort;

    @Autowired
    public CustomerController(CustomerPersistencePort customerPersistencePort) {
        this.customerPersistencePort = customerPersistencePort;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CustomerEntity>> getAll() {
        return new ResponseEntity<>(this.customerPersistencePort.getAll(), HttpStatus.OK);
    }
}
