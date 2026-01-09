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
    @NotBlank(message = "El codigo de la solicitud es obligatorio")
    private String requestCode;

    private String notes;

    private List<MultipartFile> documents;

    @NotBlank(message = "El nombre del responsable es obligatorio")
    private String responsibleName;

    private MultipartFile signatureImage;

    private String role;

}
