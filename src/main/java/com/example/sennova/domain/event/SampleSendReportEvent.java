package com.example.sennova.domain.event;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;

public class SampleSendReportEvent {
    private SampleModel sampleModel;
    private InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult;
    private byte[] pdfDocument;
    private ReportDeliverySample reportDeliverySample;


    public SampleSendReportEvent(SampleModel sampleModel, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult, byte[] pdfDocument, ReportDeliverySample reportDeliverySample) {
        this.sampleModel = sampleModel;
        this.infoResponsiblePersonReleaseResult = infoResponsiblePersonReleaseResult;
        this.pdfDocument = pdfDocument;
        this.reportDeliverySample = reportDeliverySample;
    }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public InfoResponsiblePersonReleaseResult getInfoResponsiblePersonReleaseResult() {
        return infoResponsiblePersonReleaseResult;
    }

    public byte[] getPdfDocument() {
        return pdfDocument;
    }

    public ReportDeliverySample getReportDeliverySample() {
        return reportDeliverySample;
    }
}
