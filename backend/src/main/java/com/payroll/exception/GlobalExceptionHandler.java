package com.payroll.exception;

import com.payroll.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Enterprise Global Exception Handler Controller Advice
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Intercepts all runtime exceptions thrown from Controllers, Services, or Repositories.
 * Converts exceptions into standardized JSON HTTP {@link ApiResponse} envelopes
 * with exact HTTP status codes (400, 401, 403, 404, 409, 500).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphism & Dynamic Dispatch: {@code @ExceptionHandler} routes specific subclass exceptions.
 * - Abstraction: Shields clients from internal stack traces by converting raw exceptions into clean API errors.
 * 
 * Design Patterns Used:
 * --------------------
 * - Interceptor Pattern / Aspect-Oriented Controller Advice (AOP cross-cutting concern).
 * - Generic API Envelope Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepts ResourceNotFoundException and returns HTTP 404 NOT_FOUND.
     *
     * @param ex Caught exception instance
     * @return ResponseEntity wrapping ApiResponse with status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(final ResourceNotFoundException ex) {
        log.warn("Resource Not Found: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Intercepts DuplicateResourceException and returns HTTP 409 CONFLICT.
     *
     * @param ex Caught exception instance
     * @return ResponseEntity wrapping ApiResponse with status 409
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResource(final DuplicateResourceException ex) {
        log.warn("Duplicate Resource Conflict: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Intercepts custom business logic ValidationException and returns HTTP 400 BAD_REQUEST.
     *
     * @param ex Caught exception instance
     * @return ResponseEntity wrapping ApiResponse with status 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(final ValidationException ex) {
        log.warn("Business Validation Error: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Intercepts custom UnauthorizedException or BadCredentialsException and returns HTTP 401 UNAUTHORIZED.
     *
     * @param ex Caught exception instance
     * @return ResponseEntity wrapping ApiResponse with status 401
     */
    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(final Exception ex) {
        log.warn("Unauthorized Access Attempt: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Intercepts Spring Security AccessDeniedException and returns HTTP 403 FORBIDDEN.
     *
     * @param ex Caught exception instance
     * @return ResponseEntity wrapping ApiResponse with status 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(final AccessDeniedException ex) {
        log.warn("Access Denied: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.FORBIDDEN.value(), "Access denied: Insufficient permissions for this resource."), HttpStatus.FORBIDDEN);
    }

    /**
     * Intercepts Spring DTO field validation failures ({@code @Valid}) and returns HTTP 400 with field map.
     *
     * @param ex MethodArgumentNotValidException instance
     * @return ResponseEntity wrapping ApiResponse with map of field-level validation messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        final Map<String, String> fieldErrors = new HashMap<>();
        for (final FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Request DTO Field Validation Failed: {} errors", fieldErrors.size());
        return new ResponseEntity<>(
                ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed for request parameters", fieldErrors),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Fallback exception handler for all uncaught unexpected system exceptions (HTTP 500 INTERNAL_SERVER_ERROR).
     *
     * @param ex Generic unhandled Exception instance
     * @return ResponseEntity wrapping ApiResponse with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(final Exception ex) {
        log.error("Unhandled Internal System Error: ", ex);
        return new ResponseEntity<>(
                ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected internal server error occurred: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
