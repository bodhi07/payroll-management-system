package com.payroll.member5.service.impl;

import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.member5.dto.DepartmentDTO;
import com.payroll.member5.dto.DepartmentReportDTO;
import com.payroll.member5.entity.Department;
import com.payroll.member5.mapper.DepartmentMapper;
import com.payroll.member5.repository.DepartmentRepository;
import com.payroll.member5.service.DepartmentService;
import com.payroll.response.PagedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 05: Department Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Implements department CRUD logic, validation checks, and department report aggregation.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor Injection (Never use field injection).
     */
    public DepartmentServiceImpl(final DepartmentRepository departmentRepository,
                                 final DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(final DepartmentDTO dto) {
        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Department", "code", dto.getCode());
        }
        if (departmentRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Department", "name", dto.getName());
        }

        final Department department = departmentMapper.toEntity(dto);
        final Department saved = departmentRepository.save(department);
        return departmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(final Long id, final DepartmentDTO dto) {
        final Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (!existing.getCode().equalsIgnoreCase(dto.getCode()) && departmentRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Department", "code", dto.getCode());
        }
        if (!existing.getName().equalsIgnoreCase(dto.getName()) && departmentRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Department", "name", dto.getName());
        }

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());

        final Department updated = departmentRepository.save(existing);
        return departmentMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(final Long id) {
        final Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByCode(final String code) {
        final Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DepartmentDTO> getAllDepartments(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        final Page<Department> page = departmentRepository.findAll(pageable);
        final List<DepartmentDTO> content = page.getContent().stream().map(departmentMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<DepartmentDTO>builder()
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
    public void deleteDepartment(final Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentReportDTO getDepartmentReport(final Long departmentId) {
        final Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        // Execute aggregation query over employees table
        final String countQuery = "SELECT COUNT(e) FROM Employee e WHERE e.department.id = :deptId";
        final Long empCount = entityManager.createQuery(countQuery, Long.class)
                .setParameter("deptId", departmentId)
                .getSingleResult();

        final String salaryQuery = "SELECT SUM(e.basicSalary) FROM Employee e WHERE e.department.id = :deptId";
        final BigDecimal totalSalary = entityManager.createQuery(salaryQuery, BigDecimal.class)
                .setParameter("deptId", departmentId)
                .getSingleResult();

        return DepartmentReportDTO.builder()
                .departmentId(dept.getId())
                .departmentName(dept.getName())
                .departmentCode(dept.getCode())
                .totalEmployeeCount(empCount != null ? empCount : 0L)
                .totalDepartmentSalary(totalSalary != null ? totalSalary : BigDecimal.ZERO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentReportDTO> getAllDepartmentReports() {
        final List<Department> departments = departmentRepository.findAll();
        final List<DepartmentReportDTO> reports = new ArrayList<>();
        for (final Department dept : departments) {
            reports.add(getDepartmentReport(dept.getId()));
        }
        return reports;
    }
}
