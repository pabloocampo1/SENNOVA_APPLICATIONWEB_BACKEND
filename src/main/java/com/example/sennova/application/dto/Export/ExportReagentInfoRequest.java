package com.example.sennova.application.dto.Export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@AllArgsConstructor
public class ExportReagentInfoRequest {
    private Long reagentsId;
    private String responsibleEmailDownload;
}
