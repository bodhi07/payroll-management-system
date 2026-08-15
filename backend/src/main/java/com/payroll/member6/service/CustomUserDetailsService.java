package com.payroll.member6.service;

import com.payroll.member6.entity.User;
import com.payroll.member6.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 06: Custom Spring Security UserDetailsService Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Connects Spring Security authentication framework with the MySQL `users` database table.
 * Loads user credentials and maps roles to GrantedAuthority objects.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Interface Realization (Polymorphism): Implements {@link UserDetailsService}.
 * - Dependency Injection: Constructor injection for {@link UserRepository}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Service Pattern.
 * - Adapter Pattern: Converts User entity & Role objects into Spring Security UserDetails/GrantedAuthority interfaces.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor Injection (No Field Injection).
     *
     * @param userRepository User repository bean
     */
    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String usernameOrEmail) throws UsernameNotFoundException {
        // Query database by username or email address
        final User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)));

        // Map database Role entities to Spring Security GrantedAuthority instances
        final Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // Account non-expired
                true, // Credentials non-expired
                true, // Account non-locked
                authorities
        );
    }
}
