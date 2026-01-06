package com.example.sennova.application.dto.testeRequest.ReleaaseResult;

import lombok.Data;

import java.util.List;

@Data
public class SendReportSamplesDto {
    private List<Long> samples;
    private InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult;
}
