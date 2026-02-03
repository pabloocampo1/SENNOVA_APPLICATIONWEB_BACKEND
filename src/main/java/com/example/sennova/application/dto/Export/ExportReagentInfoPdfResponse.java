package com.example.sennova.application.dto.Export;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ExportReagentInfoPdfResponse {
    private String fileName;
    private byte[] file;
}
