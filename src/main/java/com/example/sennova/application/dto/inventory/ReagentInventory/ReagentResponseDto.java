package com.example.sennova.application.dto.inventory.ReagentInventory;


import java.time.LocalDate;

public record ReagentResponseDto(
        Long reagentsId,
        String reagentName,
        String brand,
        String purity,
        Integer units,
        Double quantity,
        String unitOfMeasure,
        String senaInventoryTag,
        String responsible,
        Long locationId,
        Long usageId,
        String stateExpiration,
        String state,
        Boolean isPresent,
        String locationName,
        String usageName,
        String imageUrl,
        String batch,
        LocalDate expirationDate,
        LocalDate createAt,
        LocalDate updateAt
) {
}
