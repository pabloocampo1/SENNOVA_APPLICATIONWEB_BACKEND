package com.example.sennova.application.usecases.TestRequest;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;

import java.util.List;

public interface TestRequestReleaseResultUseCase {

    byte[] generateReleaseResultBySampleId(Long sampleId, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult);
    List<byte[]> generateReleaseResultByTestRequest(Long testRequestId);
}
