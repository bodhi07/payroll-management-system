package com.payroll.member1.mapper;

import com.payroll.member1.dto.EmployeeDTO;
import com.payroll.member1.entity.Employee;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 01: Employee Mapper Component
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts between {@link Employee} Entity and {@link EmployeeDTO}.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(final Employee employee) {
        if (employee == null) return null;
        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .nic(employee.getNic())
                .gender(employee.getGender())
                .address(employee.getAddress())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .designation(employee.getDesignation())
                .basicSalary(employee.getBasicSalary())
                .joinDate(employee.getJoinDate())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
