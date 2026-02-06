package com.example.sennova.application.dto.testeRequest;

import java.time.LocalDate;

public record ReceptionInfoRequest(
         LocalDate sampleEntryDate ,
         LocalDate sampleReceptionDate ,
        Double grossWeight,
        Double temperature,
        String packageDescription,
        String identificationSample,
        String storageConditions,
        String observations,
         String samplingLocation
) {
}




