package com.payroll.exception;

/**
 * ============================================================================
 * Custom Domain Exception: Duplicate Resource Conflict (HTTP 409)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Thrown when attempting to create a record that violates unique constraints
 * (e.g. duplicate NIC, duplicate email, duplicate employee number, or username).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Inheritance: Extends {@link RuntimeException}.
 * - Polymorphism: Constructor overloading for custom message formatting.
 * 
 * Design Patterns Used:
 * --------------------
 * - Custom Exception Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs exception with specific error message.
     *
     * @param message Detail explanation of conflict
     */
    public DuplicateResourceException(final String message) {
        super(message);
    }

    /**
     * Formats duplicate resource constraint error message.
     *
     * @param resourceName Entity name
     * @param fieldName    Unique key field name
     * @param fieldValue   Attempted value causing collision
     */
    public DuplicateResourceException(final String resourceName, final String fieldName, final Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
