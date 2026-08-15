package com.payroll.exception;

/**
 * ============================================================================
 * Custom Domain Exception: Validation Error (HTTP 400)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Thrown when business rules validation fails (e.g. invalid date ranges,
 * negative salary values, insufficient leave balance).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Inheritance: Extends {@link RuntimeException}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Custom Exception Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs exception with validation error message.
     *
     * @param message Error description
     */
    public ValidationException(final String message) {
        super(message);
    }
}
