package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestReleaseResultUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestRequestReleaseResultServiceImpl implements TestRequestReleaseResultUseCase {

    private final TestRequestUseCase testRequestUseCase;
    private final SampleUseCase sampleUseCase;
    private final ReleaseResultGeneratePdfService releaseResultGeneratePdfService;

    @Autowired
    public TestRequestReleaseResultServiceImpl(TestRequestUseCase testRequestUseCase, SampleUseCase sampleUseCase, ReleaseResultGeneratePdfService releaseResultGeneratePdfService) {
        this.testRequestUseCase = testRequestUseCase;
        this.sampleUseCase = sampleUseCase;

        this.releaseResultGeneratePdfService = releaseResultGeneratePdfService;
    }

    @Override
    public byte[] generateReleaseResultBySampleId(Long sampleId, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult) {
        try {
            SampleModel sample = this.sampleUseCase.getById(sampleId);
            CustomerModel customerModel = sample.getTestRequest().getCustomer();
            TestRequestModel testRequestModel = sample.getTestRequest();
            return this.releaseResultGeneratePdfService.generateReportBySample(sample, customerModel, testRequestModel, infoResponsiblePersonReleaseResult);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
       }

    @Override
    public List<byte[]> generateReleaseResultByTestRequest(Long testRequestId) {
        return List.of();
    }
}
