package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SampleUseCase {
    SampleModel save(SampleModel sampleModel);
    List<SampleModel> getAllByTestRequest(Long testRequest);
    void deleteById(Long sampleId);
    List<SampleModel> getAllByStatusReception();
    void deleteFileResultAnalysis(Long sampleProductDocumentResultId);
    SampleModel getById(Long id);
    SampleAnalysisModel saveResult( SampleAnalysisRequestRecord SampleAnalysisRequestRecord,  String requestCode);
    SampleModel saveReception(ReceptionInfoRequest receptionInfoRequest, Long sampleId, MultipartFile file);
    List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId);
    List<SampleProductDocumentResult> saveDocsResult(List<MultipartFile> docs, Long analysisResult);
}
