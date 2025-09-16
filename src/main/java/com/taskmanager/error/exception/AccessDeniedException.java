package com.taskmanager.error.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AccessDeniedException extends RuntimeException {

    int code = 403;
    String description;

    public AccessDeniedException(String message, String description) {
        super(message);
        this.description = description;
    }
}
