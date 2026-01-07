package com.example.sennova.domain.event;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;

public class SampleSendReportEvent {
    private Long sampleId;
    private InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult;
    private byte[] pdfDocument;
    private Long reportDeliverySampleId;

    public SampleSendReportEvent(Long sampleId, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult, byte[] pdfDocument, Long reportDeliverySampleId) {
        this.sampleId = sampleId;
        this.infoResponsiblePersonReleaseResult = infoResponsiblePersonReleaseResult;
        this.pdfDocument = pdfDocument;
        this.reportDeliverySampleId = reportDeliverySampleId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public InfoResponsiblePersonReleaseResult getInfoResponsiblePersonReleaseResult() {
        return infoResponsiblePersonReleaseResult;
    }

    public byte[] getPdfDocument() {
        return pdfDocument;
    }

    public Long getReportDeliverySampleId() {
        return reportDeliverySampleId;
    }
}
