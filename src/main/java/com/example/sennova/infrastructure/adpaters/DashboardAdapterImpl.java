package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.port.DashboardPort;
import com.example.sennova.infrastructure.persistence.repositoryJpa.DashboardRepositoryJpa;
import com.example.sennova.infrastructure.projection.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DashboardAdapterImpl implements DashboardPort {

    @Autowired
    private DashboardRepositoryJpa dashboardRepositoryJpa;



    @Override
    public Integer countActiveTestRequestCurrentMonth() {
        return this.dashboardRepositoryJpa.countActiveTestRequestCurrentMonth() ;
    }

    @Override
    public Integer countActiveTestRequestLastMonth() {
        return this.dashboardRepositoryJpa.countActiveTestRequestLastMonth();
    }

    @Override
    public Integer countTestRequestCreatedCurrentMonth() {
        return this.dashboardRepositoryJpa.countTestRequestCreatedCurrentMonth();
    }

    @Override
    public Integer countTestRequestCreatedLastMonth() {
        return this.dashboardRepositoryJpa.countTestRequestCreatedLastMonth();
    }

    @Override
    public Integer countSamplesCreatedCurrentMonth() {
        return this.dashboardRepositoryJpa.countSamplesCreatedCurrentMonth();
    }

    @Override
    public Integer countSamplesCreatedLastMonth() {
        return this.dashboardRepositoryJpa.countSamplesCreatedLastMonth();
    }

    @Override
    public Integer countSamplesExpired() {
        return this.dashboardRepositoryJpa.countSamplesExpiredAndNotSend();
    }

    @Override
    public Integer countMaintenanceEquipment() {
        return this.dashboardRepositoryJpa.countMaintenanceEquipment();
    }

    @Override
    public Integer countSamplesToExecute() {
        return this.dashboardRepositoryJpa.countSamplesToExecute();
    }

    @Override
    public Integer countQuotationPending() {
        return this.dashboardRepositoryJpa.countQuotationPending();
    }

    @Override
    public Integer countReagentExpired() {
        return this.dashboardRepositoryJpa.countExpiredReagent();
    }

    @Override
    public EquipmentsMetricsProjection getEquipmentMetrics() {
        return this.dashboardRepositoryJpa.getEquipmentsMetricsProjection();
    }

    @Override
    public ReagentsMetricsProjection getReagentMetrics() {
        return this.dashboardRepositoryJpa.getReagentMetricsProjection();
    }

    @Override
    public List<ProductsMoreUsedProjection> productsMoreUsed(LocalDateTime start, LocalDateTime end) {
        return this.dashboardRepositoryJpa.productsMoreUsed(start, end);
    }

    @Override
    public List<MatrixMoreUsedProjection> matrixMoreUsed(LocalDateTime start, LocalDateTime end) {
        return this.dashboardRepositoryJpa.matrixMoreUsed(start, end);
    }

    @Override
    public List<SamplesDeliveredLast12MonthsProjection> findSamplesDeliveredLast12Months() {
        return this.dashboardRepositoryJpa.SamplesDeliveredLast12Months();
    }

    @Override
    public SampleStatusMetricsProjection findSampleStatusMetrics(LocalDate start, LocalDate end) {
        return this.dashboardRepositoryJpa.countSampleStatusMetrics(start, end);
    }

    @Override
    public TestRequestStateMetricsProjection findTestRequestStatusMetrics(LocalDate start, LocalDate end) {
        return this.dashboardRepositoryJpa.countTestRequestState(start, end);
    }

    @Override
    public List<ResultsByUserProjection> findResultsByUser(LocalDateTime start, LocalDateTime end) {
        return this.dashboardRepositoryJpa.findResultsByUserMonthly(start, end);
    }

    @Override
    public List<MonthlyTestRequestsProjection> findTestRequestsByMonth() {
        return this.dashboardRepositoryJpa.findTestRequestsByMonth();
    }


}
