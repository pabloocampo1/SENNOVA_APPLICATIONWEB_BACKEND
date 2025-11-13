package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.EquipmentInventory.request.EquipmentLoanRequest;
import com.example.sennova.application.usecases.EquipmentLoanUseCase;
import com.example.sennova.domain.model.EquipmentLoanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan/equipment")
public class EquipmentLoanController {

    private final EquipmentLoanUseCase equipmentLoanUseCase;

    @Autowired
    public EquipmentLoanController(EquipmentLoanUseCase equipmentLoanUseCase) {
        this.equipmentLoanUseCase = equipmentLoanUseCase;
    }

    @GetMapping("/getByEquipmentId/{equipmentId}")
    public ResponseEntity<List<EquipmentLoanModel>> getAllByEquipmentId(@PathVariable("equipmentId") Long equipmentId){
        return new ResponseEntity<>(this.equipmentLoanUseCase.getAllByEquipmentId(equipmentId), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<EquipmentLoanModel> save(@RequestBody EquipmentLoanRequest equipmentLoanRequest){
      try {
          return new ResponseEntity<>(this.equipmentLoanUseCase.save(equipmentLoanRequest), HttpStatus.CREATED);
      } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
      }
    }

    @DeleteMapping("/delete/{loanId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("loanId") Long id) {

        return new ResponseEntity<>(this.equipmentLoanUseCase.deleteById(id), HttpStatus.OK);
    }
}
