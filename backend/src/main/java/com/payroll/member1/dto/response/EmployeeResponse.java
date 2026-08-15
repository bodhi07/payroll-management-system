package com.payroll.member1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing Employee response payload.
 *
 * @author Senior Java Spring Boot Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    /**
     * Unique identifier.
     */
    private Long id;

    /**
     * Unique employee code.
     */
    private String employeeCode;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Gender.
     */
    private String gender;

    /**
     * Date of birth.
     */
    private LocalDate dob;

    /**
     * Email address.
     */
    private String email;

    /**
     * Phone number.
     */
    private String phone;

    /**
     * Residential address.
     */
    private String address;

    /**
     * Basic salary.
     */
    private BigDecimal basicSalary;

    /**
     * Designation.
     */
    private String designation;

    /**
     * Active/Inactive status.
     */
    private Boolean status;

    /**
     * Associated department ID.
     */
    private Long departmentId;

    /**
     * Associated department code.
     */
    private String departmentCode;

    /**
     * Associated department name.
     */
    private String departmentName;

    /**
     * Creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last modification timestamp.
     */
    private LocalDateTime updatedAt;
}
