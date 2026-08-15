package com.payroll.member1.service.impl;

import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.member1.dto.EmployeeDTO;
import com.payroll.member1.entity.Employee;
import com.payroll.member1.mapper.EmployeeMapper;
import com.payroll.member1.repository.EmployeeRepository;
import com.payroll.member1.service.EmployeeService;
import com.payroll.member5.entity.Department;
import com.payroll.member5.repository.DepartmentRepository;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 01: Employee Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Business logic implementation for Employee Management module.
 * Provides validation checks, department associations, pagination, and linear search.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(final EmployeeRepository employeeRepository,
                               final DepartmentRepository departmentRepository,
                               final EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(final EmployeeDTO dto) {
        if (employeeRepository.existsByEmployeeNumber(dto.getEmployeeNumber())) {
            throw new DuplicateResourceException("Employee", "employeeNumber", dto.getEmployeeNumber());
        }
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", dto.getEmail());
        }
        if (employeeRepository.existsByNic(dto.getNic())) {
            throw new DuplicateResourceException("Employee", "nic", dto.getNic());
        }

        final Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        final Employee employee = Employee.builder()
                .employeeNumber(dto.getEmployeeNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .nic(dto.getNic())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .department(department)
                .designation(dto.getDesignation())
                .basicSalary(dto.getBasicSalary())
                .joinDate(dto.getJoinDate())
                .status(dto.getStatus())
                .build();

        final Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(final Long employeeId, final EmployeeDTO dto) {
        final Employee existing = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        if (!existing.getEmployeeNumber().equalsIgnoreCase(dto.getEmployeeNumber()) && employeeRepository.existsByEmployeeNumber(dto.getEmployeeNumber())) {
            throw new DuplicateResourceException("Employee", "employeeNumber", dto.getEmployeeNumber());
        }
        if (!existing.getEmail().equalsIgnoreCase(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", dto.getEmail());
        }
        if (!existing.getNic().equalsIgnoreCase(dto.getNic()) && employeeRepository.existsByNic(dto.getNic())) {
            throw new DuplicateResourceException("Employee", "nic", dto.getNic());
        }

        final Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        existing.setEmployeeNumber(dto.getEmployeeNumber());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setNic(dto.getNic());
        existing.setGender(dto.getGender());
        existing.setAddress(dto.getAddress());
        existing.setDepartment(department);
        existing.setDesignation(dto.getDesignation());
        existing.setBasicSalary(dto.getBasicSalary());
        existing.setJoinDate(dto.getJoinDate());
        existing.setStatus(dto.getStatus());

        final Employee updated = employeeRepository.save(existing);
        return employeeMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(final Long employeeId) {
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        return employeeMapper.toDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByNumber(final String employeeNumber) {
        final Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeNumber", employeeNumber));
        return employeeMapper.toDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeDTO> getAllEmployees(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        final Page<Employee> page = employeeRepository.findAll(pageable);
        final List<EmployeeDTO> content = page.getContent().stream().map(employeeMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<EmployeeDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeDTO> searchEmployees(final String query, final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        final Page<Employee> page = employeeRepository.searchEmployees(query, pageable);
        final List<EmployeeDTO> content = page.getContent().stream().map(employeeMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<EmployeeDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeDTO> filterEmployeesByDepartment(final Long departmentId, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("firstName").ascending());
        final Page<Employee> page = employeeRepository.findByDepartmentId(departmentId, pageable);
        final List<EmployeeDTO> content = page.getContent().stream().map(employeeMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<EmployeeDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeDTO> filterEmployeesByStatus(final String status, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("firstName").ascending());
        final Page<Employee> page = employeeRepository.findByStatus(status, pageable);
        final List<EmployeeDTO> content = page.getContent().stream().map(employeeMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<EmployeeDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteEmployee(final Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee", "id", employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }

    /**
     * Demonstrates manual Linear Search algorithm iteration over employee list.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> linearSearchEmployeesByName(final String keyword) {
        final List<Employee> allEmployees = employeeRepository.findAll();
        final List<EmployeeDTO> matches = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }

        final String lowerKeyword = keyword.toLowerCase();
        for (int i = 0; i < allEmployees.size(); i++) {
            final Employee emp = allEmployees.get(i);
            final String fullName = (emp.getFirstName() + " " + emp.getLastName()).toLowerCase();
            if (fullName.contains(lowerKeyword)) {
                matches.add(employeeMapper.toDTO(emp));
            }
        }
        return matches;
    }
}
