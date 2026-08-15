package com.payroll.member6.mapper;

import com.payroll.member6.dto.UserDTO;
import com.payroll.member6.entity.Role;
import com.payroll.member6.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 06: User Component Mapper
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts between User entity and UserDTO view objects.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Encapsulates entity-to-DTO conversion logic.
 * 
 * Design Patterns Used:
 * --------------------
 * - Data Mapper Pattern.
 * - Component Pattern: Managed as Spring component.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class UserMapper {

    /**
     * Maps User entity to UserDTO view object.
     *
     * @param user User database entity
     * @return Transformed UserDTO object
     */
    public UserDTO toDTO(final User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
