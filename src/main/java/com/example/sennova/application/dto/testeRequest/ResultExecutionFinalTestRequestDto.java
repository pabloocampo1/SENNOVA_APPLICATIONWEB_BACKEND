package com.example.sennova.application.dto.testeRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultExecutionFinalTestRequestDto {
    @NotNull(message = "El id de la solicitud es obligatorio")
    private Long testRequestId;

    private String notes;

    private List<MultipartFile> documents;

    @NotBlank(message = "El nombre del responsable es obligatorio")
    private String responsibleName;

    private MultipartFile signatureImage;

}
