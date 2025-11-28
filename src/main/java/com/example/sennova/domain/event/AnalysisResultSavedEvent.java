package com.example.sennova.domain.event;

public class AnalysisResultSavedEvent {
    private final String requestCode;

    public AnalysisResultSavedEvent(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestCode(){
        return requestCode;
    }
}
