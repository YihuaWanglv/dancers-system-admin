package com.dancers.service.system.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("401 Unauthorized!");
    }
}
