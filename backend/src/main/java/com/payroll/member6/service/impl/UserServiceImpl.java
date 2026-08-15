package com.payroll.member6.service.impl;

import com.payroll.exception.ResourceNotFoundException;
import com.payroll.member6.dto.UserDTO;
import com.payroll.member6.entity.Role;
import com.payroll.member6.entity.User;
import com.payroll.member6.mapper.UserMapper;
import com.payroll.member6.repository.RoleRepository;
import com.payroll.member6.repository.UserRepository;
import com.payroll.member6.service.UserService;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 06: User Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Implements user retrieval with pagination, role updates, and account removal.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserDTO> getAllUsers(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        final Page<User> usersPage = userRepository.findAll(pageable);
        final List<UserDTO> content = usersPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return PagedResponse.<UserDTO>builder()
                .content(content)
                .pageNo(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .last(usersPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public UserDTO assignRoleToUser(final Long userId, final String roleName) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        final String formattedRole = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName.toUpperCase();
        final Role role = roleRepository.findByName(formattedRole)
                .orElseGet(() -> roleRepository.save(Role.builder().name(formattedRole).description("Dynamic Role").build()));

        user.getRoles().add(role);
        final User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(final Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}
