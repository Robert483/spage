package com.thathustudio.spage.exception;


public class ServiceException extends SpageException {

    private int code;

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
