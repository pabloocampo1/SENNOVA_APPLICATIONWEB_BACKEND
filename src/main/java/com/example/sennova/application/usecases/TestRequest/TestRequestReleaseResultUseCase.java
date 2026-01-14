package com.example.sennova.application.usecases.TestRequest;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.dto.testeRequest.ResultExecutionFinalTestRequestDto;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;

import java.util.List;

public interface TestRequestReleaseResultUseCase {
    byte[] generateReleaseResultBySampleIdPreview(Long sampleId);
    byte[] generateReleaseResultBySampleId(SampleModel sampleModel, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult);


    void generateAndSendSampleReport(List<Long> samples, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult);
    void generateAndSendTestRequestReport(ResultExecutionFinalTestRequestDto resultExecutionFinalTestRequestDto);

    List<ReportDeliverySample> getHistoryDeliveryByRequestCode(String requestCode);
}
