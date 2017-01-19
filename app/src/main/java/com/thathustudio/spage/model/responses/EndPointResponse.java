package com.thathustudio.spage.model.responses;

/**
 * Created by Phung on 16/12/2016.
 */

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