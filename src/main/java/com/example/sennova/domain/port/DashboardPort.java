package com.example.sennova.domain.port;

import com.example.sennova.application.dto.dashboard.MatrixMoreUsedDto;
import com.example.sennova.infrastructure.projection.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardPort {
     Integer countActiveTestRequestCurrentMonth();
     Integer countActiveTestRequestLastMonth();

     Integer countTestRequestCreatedCurrentMonth();
     Integer countTestRequestCreatedLastMonth();

     Integer countSamplesCreatedCurrentMonth();
     Integer countSamplesCreatedLastMonth();
     Integer countSamplesExpired();
     Integer  countMaintenanceEquipment();
     Integer countSamplesToExecute();
     Integer  countQuotationPending();


     Integer countReagentExpired();

    EquipmentsMetricsProjection getEquipmentMetrics();
    ReagentsMetricsProjection getReagentMetrics();


    List<ProductsMoreUsedProjection> productsMoreUsed(LocalDateTime start, LocalDateTime end);

    List<MatrixMoreUsedProjection> matrixMoreUsed(LocalDateTime start, LocalDateTime end);

    List<SamplesDeliveredLast12MonthsProjection>  findSamplesDeliveredLast12Months();

    SampleStatusMetricsProjection findSampleStatusMetrics(LocalDate start, LocalDate end);
    TestRequestStateMetricsProjection findTestRequestStatusMetrics(LocalDate start, LocalDate end);


    List<ResultsByUserProjection> findResultsByUser(LocalDateTime start, LocalDateTime end);

    List<MonthlyTestRequestsProjection> findTestRequestsByMonth();

}
