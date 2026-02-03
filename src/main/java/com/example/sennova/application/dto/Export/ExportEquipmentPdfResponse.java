package com.example.sennova.application.dto.Export;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExportEquipmentPdfResponse {
    private String fileName;
    private byte[] file;
}
