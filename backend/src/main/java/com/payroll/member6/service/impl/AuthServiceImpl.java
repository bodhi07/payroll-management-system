package com.payroll.member6.service.impl;

import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.exception.UnauthorizedException;
import com.payroll.jwt.JwtTokenProvider;
import com.payroll.member6.dto.JwtAuthResponseDTO;
import com.payroll.member6.dto.LoginRequestDTO;
import com.payroll.member6.dto.RegisterRequestDTO;
import com.payroll.member6.dto.UserDTO;
import com.payroll.member6.entity.Role;
import com.payroll.member6.entity.User;
import com.payroll.member6.mapper.UserMapper;
import com.payroll.member6.repository.RoleRepository;
import com.payroll.member6.repository.UserRepository;
import com.payroll.member6.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 06: Authentication Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Implements {@link AuthService} business logic: authenticating credentials via
 * AuthenticationManager, generating JWT tokens via JwtTokenProvider, and saving new
 * user entity registrations with BCrypt password encryption.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphism & Realization: Implements {@link AuthService}.
 * - Encapsulation: Conceals password hashing and authentication details.
 * - Dependency Injection: Strict constructor injection for all dependent repositories/components.
 * 
 * Design Patterns Used:
 * --------------------
 * - Service Implementation Pattern.
 * - Builder Pattern: Used to construct DTO responses and user entities.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    /**
     * Constructor Injection (Never use field injection).
     */
    public AuthServiceImpl(final AuthenticationManager authenticationManager,
                           final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final PasswordEncoder passwordEncoder,
                           final JwtTokenProvider tokenProvider,
                           final UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public JwtAuthResponseDTO login(final LoginRequestDTO loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsernameOrEmail());

        // Authenticate credentials via Spring AuthenticationManager
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        // Store authentication object in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT Access Token and Refresh Token
        final String accessToken = tokenProvider.generateToken(authentication);
        final String refreshToken = tokenProvider.generateRefreshToken(authentication.getName());

        // Fetch User entity to construct comprehensive response
        final User user = userRepository.findByUsername(authentication.getName())
                .orElseGet(() -> userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authentication.getName())));

        final Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        log.info("Login successful for user: {}", user.getUsername());

        return JwtAuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public UserDTO register(final RegisterRequestDTO registerRequest) {
        log.info("Registering new user account: {}", registerRequest.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("User", "username", registerRequest.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", registerRequest.getEmail());
        }

        // Map roles or assign default ROLE_EMPLOYEE role
        final Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            final Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_EMPLOYEE").description("Employee Default Role").build()));
            roles.add(employeeRole);
        } else {
            for (final String roleName : registerRequest.getRoles()) {
                final String formattedRole = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName.toUpperCase();
                final Role role = roleRepository.findByName(formattedRole)
                        .orElseGet(() -> roleRepository.save(Role.builder().name(formattedRole).description(formattedRole + " Role").build()));
                roles.add(role);
            }
        }

        // Build User entity with BCrypt encrypted password
        final User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();

        final User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return userMapper.toDTO(savedUser);
    }

    @Override
    public JwtAuthResponseDTO refreshToken(final String refreshToken) {
        log.info("Processing refresh token request");

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        final String username = tokenProvider.getUsernameFromToken(refreshToken);
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        final String newAccessToken = tokenProvider.generateRefreshToken(username);
        final Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        return JwtAuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
