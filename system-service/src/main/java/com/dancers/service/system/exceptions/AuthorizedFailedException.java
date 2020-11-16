package com.dancers.service.system.exceptions;

public class AuthorizedFailedException extends RuntimeException {

    public AuthorizedFailedException(String message) {
        super(message == null ? "密码错误" : message);
    }
}
