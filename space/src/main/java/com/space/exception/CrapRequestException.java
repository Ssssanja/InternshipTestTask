package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CrapRequestException extends RuntimeException {
    public CrapRequestException() {
    }

    public CrapRequestException(String message) {
        super(message);
    }
}
