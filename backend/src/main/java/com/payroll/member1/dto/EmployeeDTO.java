package com.payroll.member1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 01: Employee Data Transfer Object
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object encapsulating employee input payload and API responses.
 * Implements validation rules via Jakarta Validation API.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long employeeId;

    @NotBlank(message = "Employee number is required")
    private String employeeNumber;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "NIC / SSN is required")
    private String nic;

    @NotBlank(message = "Gender is required")
    private String gender;

    private String address;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    private String departmentName;

    @NotBlank(message = "Designation title is required")
    private String designation;

    @NotNull(message = "Basic salary is required")
    @Positive(message = "Basic salary must be greater than zero")
    private BigDecimal basicSalary;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    @NotBlank(message = "Employment status is required")
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
