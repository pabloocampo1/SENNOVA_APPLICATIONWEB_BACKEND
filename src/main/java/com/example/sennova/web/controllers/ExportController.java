package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.Export.ExportEquipmentPdfResponse;
import com.example.sennova.application.dto.Export.ExportReagentInfoRequest;
import com.example.sennova.application.usecasesImpl.export.ExportEquipment;
import com.example.sennova.application.usecasesImpl.export.ExportReagentService;
import com.example.sennova.application.usecasesImpl.testRequest.ExportTestRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/export")
public class ExportController {

    @Autowired
     private ExportReagentService exportReagentService;

    @Autowired
    private ExportEquipment exportEquipment;

    @Autowired
    private ExportTestRequestData exportTestRequestData;


    @GetMapping("/equipment/excel")
    public ResponseEntity<byte[]> exportInventory() throws IOException {

        byte[] excel = exportEquipment.generateEquipmentExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    @GetMapping("/reagent/excel")
    public ResponseEntity<byte[]> exportReagentInventory() throws IOException {

        byte[] excel = exportReagentService.generateReagentExcel();

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

        @GetMapping("/equipment/pdf/{equipmentId}")
        public ResponseEntity<byte[]> exportDataEquipmentById(@PathVariable("equipmentId") Long equipmentId) {
            try {
                ExportEquipmentPdfResponse report = this.exportEquipment.exportEquipmentInfoPdf(equipmentId);
                

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFileName() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(report.getFile());

            } catch (Exception e) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    @PostMapping("/reagent/pdf")
    public ResponseEntity<byte[]> exportDataRegentById(@RequestBody() ExportReagentInfoRequest dto){
        try {
            ExportEquipmentPdfResponse report = this.exportReagentService.generateReagentInfoPdf(dto);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(report.getFile());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
