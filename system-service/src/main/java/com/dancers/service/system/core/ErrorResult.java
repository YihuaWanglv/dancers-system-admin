package com.dancers.service.system.core;


import lombok.ToString;

@ToString
public class ErrorResult {

    private int code;
    private String message;

    public ErrorResult setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
