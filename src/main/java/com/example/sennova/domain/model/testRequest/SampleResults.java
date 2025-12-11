package com.example.sennova.domain.model.testRequest;

public class SampleResults {
    private String analysis;
    private String resultFinal;

    public SampleResults(String analysis, String resultFinal) {
        this.analysis = analysis;
        this.resultFinal = resultFinal;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getResultFinal() {
        return resultFinal;
    }

    public void setResultFinal(String resultFinal) {
        this.resultFinal = resultFinal;
    }
}
