package com.payroll.exception;

/**
 * ============================================================================
 * Custom Domain Exception: Unauthorized Access (HTTP 401 / 403)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Thrown when credentials fail, JWT token is expired/invalid, or a user lacks
 * required role permissions for an operation.
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
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs exception with security error message.
     *
     * @param message Security failure explanation
     */
    public UnauthorizedException(final String message) {
        super(message);
    }
}
