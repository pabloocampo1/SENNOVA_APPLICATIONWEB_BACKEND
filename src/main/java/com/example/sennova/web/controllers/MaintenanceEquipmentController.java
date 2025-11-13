package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.EquipmentInventory.request.MaintenanceEquipmentRequest;
import com.example.sennova.application.usecases.MaintenanceEquipmentUseCase;
import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance/equipment")
public class MaintenanceEquipmentController {

    private final MaintenanceEquipmentUseCase maintenanceEquipmentUseCase;

    public MaintenanceEquipmentController(MaintenanceEquipmentUseCase maintenanceEquipmentUseCase) {
        this.maintenanceEquipmentUseCase = maintenanceEquipmentUseCase;
    }

    @PostMapping("/save")
    public ResponseEntity<MaintenanceRecordEquipmentModel> save(@RequestBody MaintenanceEquipmentRequest maintenanceEquipmentRequest){
        return new ResponseEntity<>(this.maintenanceEquipmentUseCase.save(maintenanceEquipmentRequest), HttpStatus.CREATED);
    }

    @GetMapping("/getAllByEquipmentId/{equipmentId}")
    public ResponseEntity<List<MaintenanceRecordEquipmentModel>> getAll(
            @PathVariable("equipmentId") Long equipmentId
    ){

        return new ResponseEntity<>(this.maintenanceEquipmentUseCase.getAllByEquipmentId(equipmentId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{equipmentId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("equipmentId") Long id){
        return new ResponseEntity<>(this.maintenanceEquipmentUseCase.deleteById(id), HttpStatus.OK);

    }
}
