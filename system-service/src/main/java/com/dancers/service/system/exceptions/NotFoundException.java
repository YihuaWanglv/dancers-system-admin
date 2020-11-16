package com.dancers.service.system.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Data not found!");
    }
}
