package com.safroalex.AutoServiceERP.exception;

public class CarAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CarAlreadyExistsException(String message) {
        super(message);
    }
}