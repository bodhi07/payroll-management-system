package com.payroll.member6.controller;

import com.payroll.member6.dto.JwtAuthResponseDTO;
import com.payroll.member6.dto.LoginRequestDTO;
import com.payroll.member6.dto.RegisterRequestDTO;
import com.payroll.member6.dto.UserDTO;
import com.payroll.member6.service.AuthService;
import com.payroll.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================================
 * Member 06: Authentication REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * REST controller exposing public authentication endpoints (`/api/v1/auth`) for user login,
 * registration, and refresh token validation.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Dependency Injection: Constructor Injection for {@link AuthService}.
 * - Encapsulation: Controllers map HTTP requests into service method calls.
 * 
 * Design Patterns Used:
 * --------------------
 * - REST Controller Pattern.
 * - Generic API Envelope Pattern (wrapping payloads inside {@link ApiResponse}).
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Management", description = "Endpoints for user login, registration, and JWT token refresh.")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor Injection (Never use field injection).
     *
     * @param authService AuthService business layer bean
     */
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates user credentials and issues a JWT token.
     *
     * @param loginRequest Login DTO containing username/email and password
     * @return ResponseEntity containing JwtAuthResponseDTO inside ApiResponse envelope
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates user credentials and returns a JWT access token.")
    public ResponseEntity<ApiResponse<JwtAuthResponseDTO>> login(@Valid @RequestBody final LoginRequestDTO loginRequest) {
        final JwtAuthResponseDTO tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User authenticated successfully", tokenResponse));
    }

    /**
     * Registers a new user account with BCrypt password hashing.
     *
     * @param registerRequest Registration request DTO
     * @return ResponseEntity containing registered UserDTO inside ApiResponse envelope
     */
    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Creates a new user account with BCrypt password encryption.")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody final RegisterRequestDTO registerRequest) {
        final UserDTO registeredUser = authService.register(registerRequest);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "User registered successfully", registeredUser), HttpStatus.CREATED);
    }

    /**
     * Refreshes expired JWT token using long-lived refresh token string.
     *
     * @param refreshToken Refresh token string parameter
     * @return ResponseEntity containing updated JwtAuthResponseDTO
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT Access Token", description = "Issues a new JWT access token using a valid refresh token.")
    public ResponseEntity<ApiResponse<JwtAuthResponseDTO>> refreshToken(@RequestParam("token") final String refreshToken) {
        final JwtAuthResponseDTO tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Token refreshed successfully", tokenResponse));
    }
}
