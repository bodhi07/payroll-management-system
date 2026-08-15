package com.payroll.member2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ============================================================================
 * Member 02: Check-In Request DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Captures check-in payload parameters for daily employee arrival.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private LocalDate date;

    private LocalTime checkInTime;
}
