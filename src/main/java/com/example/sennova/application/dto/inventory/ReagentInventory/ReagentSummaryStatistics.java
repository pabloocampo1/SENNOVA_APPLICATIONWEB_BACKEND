package com.example.sennova.application.dto.inventory.ReagentInventory;

public record ReagentSummaryStatistics(
        long totalReagent,
        long totalReagentExpired,
        long totalReagentWithLowStock
) {
}
