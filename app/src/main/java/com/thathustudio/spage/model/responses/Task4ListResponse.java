package com.thathustudio.spage.model.responses;

import java.util.List;

public class Task4ListResponse<T> extends EndPointResponse {
    private List<T> response;

    public List<T> getResponse() {
        return response;
    }
}
