package com.otocom.exceptions;

public class FileExistsException extends  RuntimeException {

    public FileExistsException(String message) {
        super(message);
    }
}
