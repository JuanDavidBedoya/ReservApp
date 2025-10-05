package com.reservapp.juanb.juanm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Código 400
public class CapacityExceededException extends BusinessException {
    public CapacityExceededException(String message) {
        super(message);
    }
}
