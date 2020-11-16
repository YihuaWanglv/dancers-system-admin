package com.dancers.service.system.exceptions;

public class BusinessException extends RuntimeException {
    private int code;

    public BusinessException() {
        super("服务出错了，您可以联系管理员反馈此错误");
        this.code = 500;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
