package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.dashboard.*;
import com.example.sennova.application.usecasesImpl.DashboardService;
import com.example.sennova.infrastructure.projection.SamplesDeliveredLast12MonthsProjection;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/kpis")
    public ResponseEntity<DashboardSummary> getDashboardSummary(){
        return new ResponseEntity<>(this.dashboardService.getDashboardSummary(), HttpStatus.OK);
    }

    @GetMapping("/products/analysis-count/ranking")
    public ResponseEntity<ProductAnalysisCountDto> getProductsSamplesCount(
            @RequestParam(required = false) String year
    ){
       return new ResponseEntity<>(this.dashboardService.getProductsSamplesCount(year), HttpStatus.OK);
    };

    @GetMapping("/metrics/delivered/monthly")
    public ResponseEntity<List<SamplesDeliveredLast12MonthsDto>> getSamplesDeliveryLast12Months(){
       return new ResponseEntity<>(this.dashboardService.getSamplesDeliveryLast12Months(), HttpStatus.OK);
    };

    @GetMapping("/metrics/state")
    public ResponseEntity<MetricsStateSampleTestRequest> getMetricsStateSampleAndTestRequest(@RequestParam(required = false) String year){
        return new ResponseEntity<>(this.dashboardService.getMetricsStateSampleAndTestRequest(year), HttpStatus.OK);
    }

    @GetMapping("/results-by-user")
    public ResponseEntity<List<ResultsByUserDto>> getResultsByUser(
            @RequestParam(required = false) String year
    ){
        return new ResponseEntity<>(this.dashboardService.getResultsByUser(year), HttpStatus.OK);
    }


    @GetMapping("/metrics/testRequest/monthly")
    public ResponseEntity<List<MonthlyTestRequestsDto>>   getTestRequestsByMonth(){
        return new ResponseEntity<>(this.dashboardService.getMonthlyTestRequests(), HttpStatus.OK);
    }
}
