package com.example.sennova.application.dto.inventory.EquipmentInventory.response;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record EquipmentResponseDto(
        Long equipmentId,
        String internalCode,
        String equipmentName,
        String brand,
        String model,
        String serialNumber,
        LocalDate acquisitionDate,
        Boolean markReport,
        LocalDate maintenanceDate,
         String senaInventoryTag,
        String amperage,
        String voltage,
        double equipmentCost,
        String state,
        Boolean available,
        String responsible,
        Long locationId,
        String locationName,
        Long usageId,
        String usageName,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String imageUrl,
        String description
) {
}
