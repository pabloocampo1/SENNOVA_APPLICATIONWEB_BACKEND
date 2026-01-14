package com.example.sennova.application.usecases.TestRequest;

import com.example.sennova.domain.model.testRequest.SampleModel;
import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestRequestValidatorUseCase {
     void validateDocumentsCount(List<MultipartFile> documents);
     void validateAllAnalysisComplete(List<SampleModel> samples);
}
