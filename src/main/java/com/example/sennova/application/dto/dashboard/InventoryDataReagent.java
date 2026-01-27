package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class InventoryDataReagent {
    private Long reagentsActives;
    private Long reagentsExpired;
    private Long reagentWithoutStock;
}
