package com.payroll.member6.service;

import com.payroll.member6.dto.JwtAuthResponseDTO;
import com.payroll.member6.dto.LoginRequestDTO;
import com.payroll.member6.dto.RegisterRequestDTO;
import com.payroll.member6.dto.UserDTO;

/**
 * ============================================================================
 * Member 06: Authentication Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Service contract defining user authentication, token generation, user registration,
 * and token refresh business methods.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Abstraction & Interfaces: Defines contract decoupling interface from implementation.
 * 
 * Design Patterns Used:
 * --------------------
 * - Service Interface Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface AuthService {

    /**
     * Authenticates user credentials and returns JWT Bearer token response payload.
     *
     * @param loginRequest Login DTO containing credentials
     * @return Formatted JWT authentication response object
     */
    JwtAuthResponseDTO login(LoginRequestDTO loginRequest);

    /**
     * Registers a new user account with BCrypt encrypted password and role assignments.
     *
     * @param registerRequest Registration DTO
     * @return Registered UserDTO object
     */
    UserDTO register(RegisterRequestDTO registerRequest);

    /**
     * Refreshes expired JWT access token using a valid refresh token.
     *
     * @param refreshToken Long-lived refresh token
     * @return New JWT authentication response object
     */
    JwtAuthResponseDTO refreshToken(String refreshToken);
}
