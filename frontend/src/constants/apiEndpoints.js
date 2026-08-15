/**
 * Enterprise API Endpoints Constant
 *
 * Base configuration and endpoints for Spring Boot 3 / Java 21 REST API backend.
 * Uses `/api/v1` base mapping matching all backend controllers.
 */
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

export const ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    REFRESH: '/auth/refresh-token',
  },
  EMPLOYEES: {
    BASE: '/employees',
    BY_ID: (id) => `/employees/${id}`,
    BY_NUMBER: (number) => `/employees/number/${number}`,
    SEARCH: '/employees/search',
    FILTER_DEPT: (deptId) => `/employees/filter/department/${deptId}`,
    FILTER_STATUS: (status) => `/employees/filter/status/${status}`,
    LINEAR_SEARCH: '/employees/linear-search',
  },
  PAYROLL: {
    BASE: '/payroll',
    GENERATE: '/payroll/generate',
    BY_ID: (id) => `/payroll/${id}`,
    BY_EMPLOYEE: (empId) => `/payroll/employee/${empId}`,
    BY_MONTH_YEAR: '/payroll/month-year',
    PAY: (id) => `/payroll/${id}/pay`,
    PAYSLIP: (id) => `/payroll/${id}/payslip`,
    TOTAL_BUDGET: '/payroll/total-budget',
  },
  ATTENDANCE: {
    BASE: '/attendance',
    CHECK_IN: '/attendance/check-in',
    CHECK_OUT: '/attendance/check-out',
    BY_ID: (id) => `/attendance/${id}`,
    BY_EMPLOYEE: (empId) => `/attendance/employee/${empId}`,
    BY_DATE: (date) => `/attendance/date/${date}`,
    REPORT: '/attendance/report',
  },
  LEAVE: {
    BASE: '/leaves',
    APPLY: '/leaves/apply',
    BY_ID: (id) => `/leaves/${id}`,
    APPROVE: (id) => `/leaves/${id}/approve`,
    REJECT: (id) => `/leaves/${id}/reject`,
    BY_EMPLOYEE: (empId) => `/leaves/employee/${empId}`,
    BY_STATUS: (status) => `/leaves/status/${status}`,
    BALANCE: (empId) => `/leaves/balance/${empId}`,
  },
  DEPARTMENTS: {
    BASE: '/departments',
    BY_ID: (id) => `/departments/${id}`,
    REPORT: (id) => `/departments/${id}/report`,
    ALL_REPORTS: '/departments/reports',
  },
  USERS: {
    BASE: '/users',
    BY_ID: (id) => `/users/${id}`,
    ROLES: (id) => `/users/${id}/roles`,
  },
  AUDIT: {
    BASE: '/audit-logs',
  },
  NOTIFICATIONS: {
    BASE: '/notifications',
    BY_USER: (userId) => `/notifications/user/${userId}`,
    UNREAD: (userId) => `/notifications/user/${userId}/unread`,
    MARK_READ: (id) => `/notifications/${id}/read`,
  },
};
