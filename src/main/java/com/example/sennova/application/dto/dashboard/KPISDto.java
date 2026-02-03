package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class KPISDto {
    private KPISValueDto testRequestActive;

    private KPISValueDto samplesCreated;
    private KPISValueDto testRequestCreated;
    private Integer expiredSamples;
    private Integer maintenanceEquipments;
    private Integer expiredReagent;
    private Integer countTestRequestInProcess;

    private Integer samplesToExecute;

    private Integer quotationPending;
}
