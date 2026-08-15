package com.payroll.member1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating or updating a Department request.
 *
 * @author Senior Java Spring Boot Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    /**
     * Unique code for department (e.g., DEPT-001).
     */
    @NotBlank(message = "Department code cannot be blank")
    @Size(min = 2, max = 50, message = "Department code must be between 2 and 50 characters")
    private String departmentCode;

    /**
     * Department name (3-100 characters).
     */
    @NotBlank(message = "Department name cannot be blank")
    @Size(min = 3, max = 100, message = "Department name must be between 3 and 100 characters")
    private String departmentName;

    /**
     * Optional description of the department.
     */
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    /**
     * Active status of department. Defaults to true.
     */
    private Boolean status;
}
