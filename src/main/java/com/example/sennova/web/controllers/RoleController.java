package com.example.sennova.web.controllers;

import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.domain.port.RolePersistencePort;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RolePersistencePort rolePersistencePort;

    @GetMapping("/getAll")
    public ResponseEntity<List<RoleModel>> getAll(){
        return  new ResponseEntity<>(this.rolePersistencePort.getAll(), HttpStatus.OK);
    }
}
