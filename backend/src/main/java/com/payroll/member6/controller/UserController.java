package com.payroll.member6.controller;

import com.payroll.member6.dto.UserDTO;
import com.payroll.member6.service.UserService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================================
 * Member 06: User Administration REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * REST API endpoints (`/api/v1/users`) for user management, role assignments,
 * pagination, and account removal. Protected with Role-Based Access Control (RBAC).
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user profile administration and role assignment.")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get User By ID", description = "Retrieves user details by primary key ID.")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable("id") final Long id) {
        final UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User fetched successfully", user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Users Paginated", description = "Fetches a paginated list of users.")
    public ResponseEntity<ApiResponse<PagedResponse<UserDTO>>> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) {
        
        final PagedResponse<UserDTO> response = userService.getAllUsers(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Users fetched successfully", response));
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign Role to User", description = "Assigns a security role (e.g. ROLE_HR, ROLE_ADMIN) to a user.")
    public ResponseEntity<ApiResponse<UserDTO>> assignRole(@PathVariable("id") final Long id,
                                                            @RequestParam("role") final String roleName) {
        final UserDTO updatedUser = userService.assignRoleToUser(id, roleName);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Role assigned successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete User", description = "Deletes a user account by ID.")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User deleted successfully"));
    }
}
