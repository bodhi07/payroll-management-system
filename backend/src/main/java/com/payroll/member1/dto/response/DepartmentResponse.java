package com.payroll.member1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing Department response payload.
 *
 * @author Senior Java Spring Boot Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {

    /**
     * Unique identifier.
     */
    private Long id;

    /**
     * Unique department code.
     */
    private String departmentCode;

    /**
     * Department name.
     */
    private String departmentName;

    /**
     * Department description.
     */
    private String description;

    /**
     * Active/Inactive status.
     */
    private Boolean status;

    /**
     * Total number of employees in department.
     */
    private Integer totalEmployees;

    /**
     * Creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last modification timestamp.
     */
    private LocalDateTime updatedAt;
}
