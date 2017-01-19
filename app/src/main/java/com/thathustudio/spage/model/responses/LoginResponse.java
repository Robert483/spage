package com.thathustudio.spage.model.responses;

/**
 * Created by SonPham on 1/17/2017.
 */
public class LoginResponse<T> extends EndPointResponse {
    private T response;

    public T getResponse() {
        return response;
    }
}
