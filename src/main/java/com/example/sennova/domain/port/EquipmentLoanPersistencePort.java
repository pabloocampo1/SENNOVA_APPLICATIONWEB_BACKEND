package com.example.sennova.domain.port;

import com.example.sennova.domain.model.EquipmentLoanModel;

import java.util.List;

public interface EquipmentLoanPersistencePort {
    EquipmentLoanModel save(EquipmentLoanModel equipmentLoanModel);
    List<EquipmentLoanModel> findAllByEquipmentId(Long equipmentId);
    Boolean deleteById(Long id);
    EquipmentLoanModel getById(Long id);
    Boolean existsById(Long id);
    void deleteByEquipmentId(Long equipmentId);
}
