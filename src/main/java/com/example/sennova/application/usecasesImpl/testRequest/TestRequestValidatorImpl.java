package com.example.sennova.application.usecasesImpl.testRequest;

import com.example.sennova.application.usecases.TestRequest.TestRequestValidatorUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class TestRequestValidatorImpl implements TestRequestValidatorUseCase {
    @Override
    public void validateDocumentsCount(List<MultipartFile> documents) {
        if (documents!= null
                && documents.size() >= 3) {
            throw new IllegalArgumentException(
                    "No puedes enviar más de 2 documentos para el informe final del cliente"

            );

        }
    }

    @Override
    public void validateAllAnalysisComplete(List<SampleModel> samples) {
        boolean isCompleted = samples.stream().allMatch(
                s -> s.getAnalysisEntities().stream()
                        .allMatch(SampleAnalysisModel::getStateResult)
        );

        if(!isCompleted) throw new IllegalArgumentException(("No puedes emitir el reporte final sin todos los analisis del ensayo completados."));


    }
}
