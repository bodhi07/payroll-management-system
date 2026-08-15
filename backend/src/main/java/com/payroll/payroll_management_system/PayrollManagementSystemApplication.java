package com.payroll.payroll_management_system;

import com.payroll.PayrollApplication;

/**
 * ============================================================================
 * IDE Compatibility Bootstrapper Class
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Delegates execution to {@link com.payroll.PayrollApplication}.
 * Prevents ClassNotFoundException when launching the application via IDE run configurations
 * targeting com.payroll.payroll_management_system.PayrollManagementSystemApplication.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class PayrollManagementSystemApplication {

    public static void main(String[] args) {
        PayrollApplication.main(args);
    }
}
