package com.payroll.member1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating an Employee request.
 *
 * @author Senior Java Spring Boot Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    /**
     * Unique code for employee (e.g., EMP-001).
     */
    @NotBlank(message = "Employee code cannot be blank")
    private String employeeCode;

    /**
     * Employee first name.
     */
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    /**
     * Employee last name.
     */
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    /**
     * Gender (e.g., MALE, FEMALE, OTHER).
     */
    private String gender;

    /**
     * Date of birth.
     */
    private LocalDate dob;

    /**
     * Email address.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email format is invalid")
    private String email;

    /**
     * Sri Lankan format phone number validation (e.g., +94771234567 or 0771234567).
     */
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^(\\+94|0)?[7][0-9]{8}$", message = "Phone number must follow Sri Lankan format (e.g., 0771234567 or +94771234567)")
    private String phone;

    /**
     * Residential address.
     */
    private String address;

    /**
     * Basic salary amount (must be positive).
     */
    @NotNull(message = "Basic salary is required")
    @Positive(message = "Basic salary must be a positive value")
    private BigDecimal basicSalary;

    /**
     * Job designation.
     */
    @NotBlank(message = "Designation cannot be blank")
    private String designation;

    /**
     * Active/Inactive status. Defaults to true.
     */
    private Boolean status;

    /**
     * ID of assigned department.
     */
    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
