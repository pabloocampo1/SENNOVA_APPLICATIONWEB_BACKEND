package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class InventoryData {
    private InventoryDataEquipment equipment;
    private InventoryDataReagent reagent;
}
