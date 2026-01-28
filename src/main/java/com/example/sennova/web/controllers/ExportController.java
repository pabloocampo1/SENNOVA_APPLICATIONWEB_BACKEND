package com.example.sennova.web.controllers;

import com.example.sennova.application.usecasesImpl.ExportService;
import com.example.sennova.application.usecasesImpl.testRequest.ExportTestRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/export")
public class ExportController {

    @Autowired
     private  ExportService exportService;
    @Autowired
    private ExportTestRequestData exportTestRequestData;


    @GetMapping("/equipment/excel")
    public ResponseEntity<byte[]> exportInventory() throws IOException {

        byte[] excel = exportService.generateEquipmentExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    @GetMapping("/reagent/excel")
    public ResponseEntity<byte[]> exportReagentInventory() throws IOException {

        byte[] excel = exportService.generateReagentExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    @GetMapping("/testRequest/{year}")
    public ResponseEntity<byte[]> exportTestRequestAudit(
            @PathVariable(required = false) String year
    ) throws IOException {

        byte[] excel = exportTestRequestData.generateAnnualAuditReport(year);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

}
