package com.ecommerce.common.exception;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.response.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * Provides consistent error responses across all services.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        ErrorDetails error = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {

        log.warn("Duplicate resource: {}", ex.getMessage());

        ErrorDetails error = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            ValidationException ex, HttpServletRequest request) {

        log.warn("Validation error: {}", ex.getMessage());

        ErrorDetails error = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .fieldErrors(ex.getErrors())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validation failed: {}", fieldErrors);

        ErrorDetails error = ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        log.error("Business error: {}", ex.getMessage());

        ErrorDetails error = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error: ", ex);

        ErrorDetails error = ErrorDetails.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(error));
    }
}