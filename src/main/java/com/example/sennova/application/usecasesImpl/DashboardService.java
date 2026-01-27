package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.dashboard.*;
import com.example.sennova.domain.constants.Trend;
import com.example.sennova.domain.port.*;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import com.example.sennova.infrastructure.projection.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

     private final DashboardPort dashboardPort;
     private final ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa;

     @Autowired
    public DashboardService(DashboardPort dashboardPort, ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa) {
        this.dashboardPort = dashboardPort;
         this.reportDeliveryStatusRepositoryJpa = reportDeliveryStatusRepositoryJpa;
     }

    public DashboardSummary getDashboardSummary(){
        KPISDto kpisDto = new KPISDto();

        // test request active
        KPISValueDto kpisValueTestRequest = this.buildKpi(
                this.dashboardPort.countActiveTestRequestCurrentMonth(),
                this.dashboardPort.countActiveTestRequestLastMonth()
        );
         kpisDto.setTestRequestActive(kpisValueTestRequest);

         // samples created
        KPISValueDto kpisValueSamples = this.buildKpi(
                this.dashboardPort.countSamplesCreatedCurrentMonth(),
                this.dashboardPort.countSamplesCreatedLastMonth()
        );
        kpisDto.setSamplesCreated(kpisValueSamples);

        // test request create
        KPISValueDto kpisValueTestRequestCreate = this.buildKpi(
                this.dashboardPort.countTestRequestCreatedCurrentMonth(),
                this.dashboardPort.countTestRequestCreatedLastMonth()
        );
        kpisDto.setTestRequestCreated(kpisValueTestRequestCreate);
        kpisDto.setMaintenanceEquipments(this.dashboardPort.countMaintenanceEquipment());
        kpisDto.setExpiredReagent(this.dashboardPort.countReagentExpired());
        kpisDto.setExpiredSamples(this.dashboardPort.countSamplesExpired());
        kpisDto.setSamplesToExecute(this.dashboardPort.countSamplesToExecute());
        kpisDto.setQuotationPending(this.dashboardPort.countQuotationPending());


        // inventory data
        InventoryData inventoryData = new InventoryData();

        EquipmentsMetricsProjection equipmentsMetricsProjection = this.dashboardPort.getEquipmentMetrics();
        ReagentsMetricsProjection reagentsMetricsProjection = this.dashboardPort.getReagentMetrics();

        InventoryDataEquipment inventoryDataEquipment = new InventoryDataEquipment();
        inventoryDataEquipment.setEquipmentsActives(equipmentsMetricsProjection.getActive());
        inventoryDataEquipment.setEquipmentReported(equipmentsMetricsProjection.getReported());
        inventoryDataEquipment.setEquipmentMaintenance(equipmentsMetricsProjection.getToMaintenance());


        InventoryDataReagent inventoryDataReagent = new InventoryDataReagent();
        inventoryDataReagent.setReagentsActives(
                reagentsMetricsProjection.getActives()
        );
        inventoryDataReagent.setReagentsExpired(
                reagentsMetricsProjection.getExpired()
        );
        inventoryDataReagent.setReagentWithoutStock(
                reagentsMetricsProjection.getWithoutStock()
        );


        inventoryData.setEquipment(inventoryDataEquipment);
        inventoryData.setReagent(inventoryDataReagent);


        // dashboardSummary

        DashboardSummary dashboardSummary = new DashboardSummary();
        dashboardSummary.setKpisDto(kpisDto);
        dashboardSummary.setLastReportsSend(this.reportDeliveryStatusRepositoryJpa.findTop10ByOrderByCreatedAtDesc());
        dashboardSummary.setInventoryData(inventoryData);
        


        return  dashboardSummary;
    }

    private KPISValueDto buildKpi(Integer current, Integer previous) {

        KPISValueDto dto = new KPISValueDto();
        dto.setCurrentValue(current);
        dto.setPreviousValue(previous);

        if (previous == null || previous == 0) {
            dto.setPercentageChange(null);
            dto.setTrend(Trend.IGUAL.name());
            return dto;
        }

        double percentage = ((double) (current - previous) / previous) * 100;
        dto.setPercentageChange(Math.round(percentage * 100.0) / 100.0);

        if (current > previous) {
            dto.setTrend(Trend.SUBIO.name());
        } else if (current < previous) {
            dto.setTrend(Trend.BAJO.name());
        } else {
            dto.setTrend(Trend.IGUAL.name());
        }

        return dto;
    }

    public ProductAnalysisCountDto getProductsSamplesCount(String year){


         // if the user not select the real, the system use the current year
        if (year == null || year.isEmpty()) {
            int currentYear = LocalDate.now().getYear();
            year = String.valueOf(currentYear);
        }
        LocalDateTime start = LocalDateTime.of(Integer.parseInt(year), 1, 1, 0, 0, 0);
        LocalDateTime end   = LocalDateTime.of(Integer.parseInt(year), 12, 31, 23, 59, 59);

        ProductAnalysisCountDto productAnalysisCountDto = new ProductAnalysisCountDto();

       // products more used
        List<ProductsMoreUsedProjection> productsMoreUsedProjection = this.dashboardPort.productsMoreUsed(start, end);
        List<ProductsMoreUsedDto> productsMoreUsedDtos = productsMoreUsedProjection.stream().map(p -> {
            ProductsMoreUsedDto productsMoreUsedDto = new ProductsMoreUsedDto();
             productsMoreUsedDto.setProductName(p.getProductName());
             productsMoreUsedDto.setTotalAnalysis(p.getTotalAnalysis());
             return productsMoreUsedDto;

        }).toList();

        productAnalysisCountDto.setProductsMoreUsed(productsMoreUsedDtos);

        // matrix more used
        List<MatrixMoreUsedProjection>  matrixMoreUsedProjectionList = this.dashboardPort.matrixMoreUsed(start, end);
        List<MatrixMoreUsedDto> matrixMoreUsedDtoList = matrixMoreUsedProjectionList.stream().map(m -> {
            MatrixMoreUsedDto matrixMoreUsedDto = new MatrixMoreUsedDto();
            matrixMoreUsedDto.setTotal(m.getTotal());
            matrixMoreUsedDto.setMatrix(m.getMatrix());

            return matrixMoreUsedDto; 
        }).toList();

        productAnalysisCountDto.setMatrixMoreUsed(matrixMoreUsedDtoList);
       
       return productAnalysisCountDto;

    }

    public List<SamplesDeliveredLast12MonthsDto> getSamplesDeliveryLast12Months(){
          List<SamplesDeliveredLast12MonthsProjection> samplesDeliveredLast12MonthsProjections = this.dashboardPort.findSamplesDeliveredLast12Months();
          List<SamplesDeliveredLast12MonthsDto> samplesDeliveredLast12MonthsList = samplesDeliveredLast12MonthsProjections
                  .stream()
                  .map(s -> {
                      SamplesDeliveredLast12MonthsDto samplesDeliveredLast12MonthsDto = new SamplesDeliveredLast12MonthsDto();
                      samplesDeliveredLast12MonthsDto.setDeliveryDate(s.getDeliveryDate());
                      samplesDeliveredLast12MonthsDto.setTotal(s.getTotal());
                      return samplesDeliveredLast12MonthsDto;
                  }) .toList();

          return  samplesDeliveredLast12MonthsList;
    }


    public MetricsStateSampleTestRequest getMetricsStateSampleAndTestRequest(String year){
        if (year == null || year.isEmpty()) {
            int currentYear = LocalDate.now().getYear();
            year = String.valueOf(currentYear);
        }
       LocalDate start = LocalDate.of(Integer.parseInt(year), 1, 1);
       LocalDate end = LocalDate.of(Integer.parseInt(year), 12, 31);


       // sample metrics

        SampleStatusMetricsProjection sampleProjection = this.dashboardPort.findSampleStatusMetrics(start, end);

        SampleStatusMetricsDTO sampleDto = new SampleStatusMetricsDTO();
        sampleDto.setDelivered(sampleProjection.getDelivered());
        sampleDto.setInProcess(sampleProjection.getProcess());
        sampleDto.setOverDue(sampleProjection.getOverDue());
        sampleDto.setWithoutReception(sampleProjection.getWithoutReception());


        // test request metrics

        TestRequestStateMetricsProjection testRequestProjection = this.dashboardPort.findTestRequestStatusMetrics(start, end);

        TestRequestStateMetricsDTO testRequestDto = new TestRequestStateMetricsDTO();

        testRequestDto.setAccepted(testRequestProjection.getAccepted());
        testRequestDto.setRejected(testRequestProjection.getRejected());
        testRequestDto.setPending(testRequestProjection.getPending());


        return new MetricsStateSampleTestRequest(sampleDto, testRequestDto);
    }


   public List<ResultsByUserDto> getResultsByUser(String year){

        if (year == null || year.isEmpty()) {
            int currentYear = LocalDate.now().getYear();
            year = String.valueOf(currentYear);
        }
        LocalDateTime start = LocalDateTime.of(Integer.parseInt(year), 1, 1, 0, 0, 0);
        LocalDateTime end   = LocalDateTime.of(Integer.parseInt(year), 12, 31, 23, 59, 59);


        List<ResultsByUserProjection> resultsProjection = this.dashboardPort.findResultsByUser(start, end);


        return resultsProjection
                .stream()
                .map(r -> {
                    ResultsByUserDto result = new ResultsByUserDto();
                    result.setName(r.getName());
                    result.setYear(r.getYear());
                    result.setMonth(r.getMonth());
                    result.setTotal(r.getTotal());
                    return result;
                })
                .toList();

    }


   public  List<MonthlyTestRequestsDto>  getMonthlyTestRequests( ){
        List<MonthlyTestRequestsProjection> projections = this.dashboardPort.findTestRequestsByMonth();

        return projections.stream()
                .map(p -> {
                   return new MonthlyTestRequestsDto(p.getPeriod(), p.getTotal());

                })
                .toList();
    }



}
