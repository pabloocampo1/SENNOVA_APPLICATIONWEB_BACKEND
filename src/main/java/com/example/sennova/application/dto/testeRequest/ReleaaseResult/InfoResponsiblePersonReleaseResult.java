package com.example.sennova.application.dto.testeRequest.ReleaaseResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class InfoResponsiblePersonReleaseResult {
    private String name;
    private String Role;
    private MultipartFile signature;
}
