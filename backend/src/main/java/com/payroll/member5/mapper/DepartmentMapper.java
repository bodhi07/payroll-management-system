package com.payroll.member5.mapper;

import com.payroll.member5.dto.DepartmentDTO;
import com.payroll.member5.entity.Department;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 05: Department Mapper Component
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Handles bidirectional mapping between Department Entity and DepartmentDTO.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class DepartmentMapper {

    public DepartmentDTO toDTO(final Department department) {
        if (department == null) return null;
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    public Department toEntity(final DepartmentDTO dto) {
        if (dto == null) return null;
        return Department.builder()
                .id(dto.getId())
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .build();
    }
}
