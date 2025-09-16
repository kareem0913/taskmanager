package com.taskmanager.error;

import com.taskmanager.error.exception.AccessDeniedException;
import com.taskmanager.error.exception.BadRequestException;
import com.taskmanager.error.exception.ResourceNotFoundException;
import com.taskmanager.error.model.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.taskmanager.util.Util.currentTimestamp;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleNotFoundException(ResourceNotFoundException e) {
        return new ErrorResponse(e.getCode(), e.getMessage(), e.getDescription(), currentTimestamp());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                400,
                "Validation failed",
                errors,
                currentTimestamp()
        );
        return response;
    }

    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return new ErrorResponse(e.getCode(), e.getMessage(), e.getDescription(), currentTimestamp());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(e.getCode(), e.getMessage(), e.getDescription(), currentTimestamp());
    }

}
