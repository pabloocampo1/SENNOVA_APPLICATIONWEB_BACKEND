package com.example.sennova.application.dto.dashboard;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import lombok.Data;

import java.util.List;

@Data
public class DashboardSummary {
    private KPISDto kpisDto;
    private List<ReportDeliverySample> lastReportsSend;
    private InventoryData inventoryData;
}



        