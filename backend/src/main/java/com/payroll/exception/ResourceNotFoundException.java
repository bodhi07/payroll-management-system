package com.payroll.exception;

/**
 * ============================================================================
 * Custom Domain Exception: Resource Not Found (HTTP 404)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Thrown when a requested entity (e.g. Employee, Department, Attendance, Payroll)
 * is not found in the database by its primary key or lookup code.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Inheritance: Extends {@link RuntimeException} (unchecked domain exception).
 * - Polymorphism: Constructor overloading allows passing string messages or resource lookup details.
 * 
 * Design Patterns Used:
 * --------------------
 * - Custom Exception Pattern: Domain-specific exception throwing.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs exception with specific error message.
     *
     * @param message Detailed explanation of missing resource
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Helper constructor to format resource lookup messages automatically.
     *
     * @param resourceName Entity name (e.g. "Employee")
     * @param fieldName    Lookup field (e.g. "id")
     * @param fieldValue   Lookup value (e.g. 101)
     */
    public ResourceNotFoundException(final String resourceName, final String fieldName, final Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
