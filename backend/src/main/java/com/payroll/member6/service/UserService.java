package com.payroll.member6.service;

import com.payroll.member6.dto.UserDTO;
import com.payroll.response.PagedResponse;

/**
 * ============================================================================
 * Member 06: User Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Service contract for User administration, retrieval, role assignment, and deletion.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface UserService {

    UserDTO getUserById(Long id);

    UserDTO getUserByUsername(String username);

    PagedResponse<UserDTO> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

    UserDTO assignRoleToUser(Long userId, String roleName);

    void deleteUser(Long id);
}
