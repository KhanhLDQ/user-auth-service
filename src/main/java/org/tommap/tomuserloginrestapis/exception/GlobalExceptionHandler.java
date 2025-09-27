package org.tommap.tomuserloginrestapis.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.error(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistedException.class)
    @ResponseStatus(CONFLICT)
    public ApiResponse<Void> handleResourceAlreadyExistedException(ResourceAlreadyExistedException ex) {
        return ApiResponse.error(CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(NOT_FOUND.value(), ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        String errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponse.error(BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponse.error(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(EmailVerificationTokenExpiredException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<Void> handleEmailVerificationTokenExpiredException(EmailVerificationTokenExpiredException ex) {
        return ApiResponse.error(BAD_REQUEST.value(), ex.getMessage());
    }
}
