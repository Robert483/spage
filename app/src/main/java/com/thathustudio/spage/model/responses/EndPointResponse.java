package com.thathustudio.spage.model.responses;

public class EndPointResponse {
    private String result;
    private ErrorResult error;

    public String getResult() {
        return result;
    }

    public ErrorResult getError() {
        return error;
    }
}