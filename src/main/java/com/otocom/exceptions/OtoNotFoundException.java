package com.otocom.exceptions;

public class OtoNotFoundException extends RuntimeException {
    public OtoNotFoundException(String message) {
        super(message);
    }
}
