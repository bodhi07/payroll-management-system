package com.payroll.config;

import com.payroll.member1.entity.Employee;
import com.payroll.member1.repository.EmployeeRepository;
import com.payroll.member2.entity.Attendance;
import com.payroll.member2.repository.AttendanceRepository;
import com.payroll.member3.entity.Payroll;
import com.payroll.member3.entity.SalaryDetails;
import com.payroll.member3.repository.PayrollRepository;
import com.payroll.member4.entity.LeaveRequest;
import com.payroll.member4.repository.LeaveRepository;
import com.payroll.member5.entity.Department;
import com.payroll.member5.repository.DepartmentRepository;
import com.payroll.member6.entity.AuditLog;
import com.payroll.member6.entity.Notification;
import com.payroll.member6.entity.Role;
import com.payroll.member6.entity.User;
import com.payroll.member6.repository.AuditLogRepository;
import com.payroll.member6.repository.NotificationRepository;
import com.payroll.member6.repository.RoleRepository;
import com.payroll.member6.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ============================================================================
 * Enterprise Sample Mock Data Initializer Component
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Automatically populates the database with initial mock sample data upon startup.
 * Creates default roles (ROLE_ADMIN, ROLE_HR, ROLE_EMPLOYEE), default user 'agrani'
 * with password 'pass1234', sample departments, employees, attendance records,
 * leave applications, payroll payslips, audit logs, and notifications for Swagger UI testing.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(final RoleRepository roleRepository,
                           final UserRepository userRepository,
                           final DepartmentRepository departmentRepository,
                           final EmployeeRepository employeeRepository,
                           final AttendanceRepository attendanceRepository,
                           final LeaveRepository leaveRepository,
                           final PayrollRepository payrollRepository,
                           final NotificationRepository notificationRepository,
                           final AuditLogRepository auditLogRepository,
                           final PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
        this.payrollRepository = payrollRepository;
        this.notificationRepository = notificationRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing Enterprise Payroll System mock sample data...");

        // 1. Seed Roles
        final Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").description("Administrator Role").build()));
        final Role hrRole = roleRepository.findByName("ROLE_HR")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_HR").description("Human Resources Manager").build()));
        final Role empRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_EMPLOYEE").description("Standard Employee").build()));

        // 2. Seed Requested User: username="agrani", password="pass1234"
        if (!userRepository.existsByUsername("agrani")) {
            final Set<Role> agraniRoles = new HashSet<>();
            agraniRoles.add(adminRole);
            agraniRoles.add(hrRole);
            agraniRoles.add(empRole);

            final User agraniUser = User.builder()
                    .username("agrani")
                    .email("agrani@payroll.com")
                    .password(passwordEncoder.encode("pass1234"))
                    .enabled(true)
                    .roles(agraniRoles)
                    .build();
            userRepository.save(agraniUser);
            log.info("Created primary user: agrani / pass1234 (ROLE_ADMIN)");
        }

        // Seed Secondary User for testing (john.doe / pass1234)
        if (!userRepository.existsByUsername("john.doe")) {
            final Set<Role> userRoles = new HashSet<>();
            userRoles.add(empRole);

            final User johnUser = User.builder()
                    .username("john.doe")
                    .email("john.doe@payroll.com")
                    .password(passwordEncoder.encode("pass1234"))
                    .enabled(true)
                    .roles(userRoles)
                    .build();
            userRepository.save(johnUser);
        }

        // 3. Seed Departments
        final Department itDept = departmentRepository.findByCode("DEPT-IT")
                .orElseGet(() -> departmentRepository.save(Department.builder().name("Information Technology").code("DEPT-IT").description("Software Architecture & Engineering").build()));
        final Department hrDept = departmentRepository.findByCode("DEPT-HR")
                .orElseGet(() -> departmentRepository.save(Department.builder().name("Human Resources").code("DEPT-HR").description("Talent & HR Operations").build()));
        final Department finDept = departmentRepository.findByCode("DEPT-FIN")
                .orElseGet(() -> departmentRepository.save(Department.builder().name("Finance & Accounting").code("DEPT-FIN").description("Corporate Payroll & Budgeting").build()));

        // 4. Seed Employees
        final Employee empAgrani = employeeRepository.findByEmployeeNumber("EMP-2026-001")
                .orElseGet(() -> employeeRepository.save(Employee.builder()
                        .employeeNumber("EMP-2026-001")
                        .firstName("Agrani")
                        .lastName("Perera")
                        .email("agrani@payroll.com")
                        .phone("+94771234567")
                        .nic("200012345678")
                        .gender("MALE")
                        .address("123 Enterprise Avenue, Colombo 03")
                        .department(itDept)
                        .designation("Senior Software Architect")
                        .basicSalary(new BigDecimal("150000.00"))
                        .joinDate(LocalDate.of(2022, 1, 15))
                        .status("ACTIVE")
                        .build()));

        final Employee empJohn = employeeRepository.findByEmployeeNumber("EMP-2026-002")
                .orElseGet(() -> employeeRepository.save(Employee.builder()
                        .employeeNumber("EMP-2026-002")
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@payroll.com")
                        .phone("+94779876543")
                        .nic("199512345678")
                        .gender("MALE")
                        .address("45 Palm Grove Road, Kandy")
                        .department(hrDept)
                        .designation("HR Executive")
                        .basicSalary(new BigDecimal("120000.00"))
                        .joinDate(LocalDate.of(2023, 3, 10))
                        .status("ACTIVE")
                        .build()));

        final Employee empSarah = employeeRepository.findByEmployeeNumber("EMP-2026-003")
                .orElseGet(() -> employeeRepository.save(Employee.builder()
                        .employeeNumber("EMP-2026-003")
                        .firstName("Sarah")
                        .lastName("Connor")
                        .email("sarah.c@payroll.com")
                        .phone("+94714567890")
                        .nic("199812345678")
                        .gender("FEMALE")
                        .address("88 Galle Road, Mount Lavinia")
                        .department(finDept)
                        .designation("Senior Accountant")
                        .basicSalary(new BigDecimal("180000.00"))
                        .joinDate(LocalDate.of(2021, 6, 1))
                        .status("ACTIVE")
                        .build()));

        // 5. Seed Attendance Logs
        final LocalDate today = LocalDate.now();
        if (!attendanceRepository.existsByEmployeeEmployeeIdAndDate(empAgrani.getEmployeeId(), today)) {
            attendanceRepository.save(Attendance.builder()
                    .employee(empAgrani)
                    .date(today)
                    .checkInTime(LocalTime.of(8, 25))
                    .checkOutTime(LocalTime.of(17, 0))
                    .workingHours(8.58)
                    .lateHours(0.0)
                    .overtimeHours(0.58)
                    .status("PRESENT")
                    .build());
        }

        final LocalDate yesterday = today.minusDays(1);
        if (!attendanceRepository.existsByEmployeeEmployeeIdAndDate(empJohn.getEmployeeId(), yesterday)) {
            attendanceRepository.save(Attendance.builder()
                    .employee(empJohn)
                    .date(yesterday)
                    .checkInTime(LocalTime.of(8, 45))
                    .checkOutTime(LocalTime.of(17, 15))
                    .workingHours(8.5)
                    .lateHours(0.25)
                    .overtimeHours(0.5)
                    .status("LATE")
                    .build());
        }

        // 6. Seed Leave Requests
        if (leaveRepository.findByEmployeeEmployeeIdAndStatus(empAgrani.getEmployeeId(), "APPROVED").isEmpty()) {
            leaveRepository.save(LeaveRequest.builder()
                    .employee(empAgrani)
                    .leaveType("ANNUAL")
                    .startDate(today.plusDays(5))
                    .endDate(today.plusDays(7))
                    .totalDays(3)
                    .reason("Annual family vacation")
                    .status("APPROVED")
                    .actionReason("Approved by HR")
                    .approvedBy("agrani")
                    .build());
        }

        if (leaveRepository.findByEmployeeEmployeeIdAndStatus(empJohn.getEmployeeId(), "PENDING").isEmpty()) {
            leaveRepository.save(LeaveRequest.builder()
                    .employee(empJohn)
                    .leaveType("CASUAL")
                    .startDate(today.plusDays(10))
                    .endDate(today.plusDays(11))
                    .totalDays(2)
                    .reason("Personal urgent matters")
                    .status("PENDING")
                    .build());
        }

        // 7. Seed Sample Payroll Payslips
        final int currentMonth = today.getMonthValue();
        final int currentYear = today.getYear();
        if (!payrollRepository.existsByEmployeeEmployeeIdAndPayMonthAndPayYear(empAgrani.getEmployeeId(), currentMonth, currentYear)) {
            final BigDecimal basic = empAgrani.getBasicSalary();
            final BigDecimal allowance = new BigDecimal("25000.00");
            final BigDecimal bonus = new BigDecimal("15000.00");
            final BigDecimal gross = basic.add(allowance).add(bonus);

            final BigDecimal epfEmp = basic.multiply(new BigDecimal("0.08"));
            final BigDecimal epfEmployer = basic.multiply(new BigDecimal("0.12"));
            final BigDecimal etfEmployer = basic.multiply(new BigDecimal("0.03"));
            final BigDecimal tax = gross.multiply(new BigDecimal("0.06"));
            final BigDecimal totalDeduction = epfEmp.add(tax);
            final BigDecimal net = gross.subtract(totalDeduction);

            final Payroll payroll = Payroll.builder()
                    .employee(empAgrani)
                    .payMonth(currentMonth)
                    .payYear(currentYear)
                    .basicSalary(basic)
                    .allowance(allowance)
                    .bonus(bonus)
                    .grossSalary(gross)
                    .totalDeduction(totalDeduction)
                    .tax(tax)
                    .epfEmployee(epfEmp)
                    .epfEmployer(epfEmployer)
                    .etfEmployer(etfEmployer)
                    .loanDeduction(BigDecimal.ZERO)
                    .advanceDeduction(BigDecimal.ZERO)
                    .netSalary(net)
                    .status("PAID")
                    .paidDate(today.minusDays(2))
                    .build();

            final SalaryDetails details = SalaryDetails.builder()
                    .payroll(payroll)
                    .allowanceBreakdown("Transport: 15,000 | Medical: 10,000")
                    .bonusDescription("Performance Bonus Q2")
                    .deductionReason("Standard EPF (8%) & Tax (6%)")
                    .taxPercentage(6.0)
                    .payslipNumber("PAYSLIP-" + currentYear + "-" + String.format("%02d", currentMonth) + "-001")
                    .build();

            payroll.setSalaryDetails(details);
            payrollRepository.save(payroll);
        }

        // 8. Seed Notifications & Audit Logs
        final User userAgrani = userRepository.findByUsername("agrani").orElse(null);
        if (userAgrani != null && notificationRepository.findByUserIdAndRead(userAgrani.getId(), false).isEmpty()) {
            notificationRepository.save(Notification.builder()
                    .userId(userAgrani.getId())
                    .title("Payslip Generated")
                    .message("Your monthly payslip for " + currentMonth + "/" + currentYear + " is ready for viewing.")
                    .read(false)
                    .build());
        }

        if (auditLogRepository.findAll().isEmpty()) {
            auditLogRepository.save(AuditLog.builder()
                    .username("SYSTEM")
                    .action("SYSTEM_INIT")
                    .entityName("DataInitializer")
                    .details("Mock sample data seeded successfully for Swagger testing.")
                    .ipAddress("127.0.0.1")
                    .build());
        }

        log.info("Enterprise mock sample data initialized successfully!");
    }
}
