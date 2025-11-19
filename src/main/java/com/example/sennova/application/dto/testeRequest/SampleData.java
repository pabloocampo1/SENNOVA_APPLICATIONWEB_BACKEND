package com.example.sennova.application.dto.testeRequest;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import lombok.Data;

import java.util.List;

@Data
public class SampleData {
    private SampleModel sample;
    private List<SampleAnalysisModel> analysis;
}
