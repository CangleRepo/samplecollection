package com.ljzh.samplecollection.framwork.exception;

import com.ljzh.samplecollection.framwork.constant.ResponseEnum;

public class CustomException extends RuntimeException {
    private ResponseEnum responseEnum;

    public CustomException(ResponseEnum responseEnum) {
        this.responseEnum=responseEnum;
    }

    public CustomException(String message) {
        super(message);
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public void setResponseEnum(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }
}
