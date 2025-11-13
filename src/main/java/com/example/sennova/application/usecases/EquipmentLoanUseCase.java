package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.inventory.EquipmentInventory.request.EquipmentLoanRequest;
import com.example.sennova.domain.model.EquipmentLoanModel;

import java.util.List;

public interface EquipmentLoanUseCase {
    EquipmentLoanModel save(EquipmentLoanRequest equipmentLoanRequest);
    List<EquipmentLoanModel> getAllByEquipmentId(Long equipmentId);
    Boolean deleteById(Long id);
}
