package org.example.biluthyrningssystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnalterableFieldException extends RuntimeException { // Created by Leo to prevent SSN & ID changes
    private final String message;
    private final Object fieldValue;

    public UnalterableFieldException(String message) {
        this.message = message;
        this.fieldValue = null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
