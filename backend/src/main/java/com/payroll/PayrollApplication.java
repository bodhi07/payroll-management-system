package com.payroll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * ============================================================================
 * Enterprise Payroll Management System - Main Application Bootstrapper
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Central entry point for bootstrapping Spring Boot. Includes Database Connectivity Health Check
 * upon startup to log terminal database connection status.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class PayrollApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PayrollApplication.class, args);
    }

    /**
     * Database Connectivity Check Bean. Runs on application startup and logs database connection status.
     *
     * @param dataSource Injected Hikari DataSource bean
     * @return CommandLineRunner instance
     */
    @Bean
    public CommandLineRunner checkDatabaseConnection(final DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                final DatabaseMetaData metaData = connection.getMetaData();
                final String statusBanner = String.format(
                        "\n======================================================================\n" +
                        " [DATABASE CONNECTED SUCCESSFULLY]\n" +
                        " Database Product : %s %s\n" +
                        " Database URL     : %s\n" +
                        " Database User    : %s\n" +
                        " Status           : ACTIVE & OPERATIONAL\n" +
                        "======================================================================\n",
                        metaData.getDatabaseProductName(),
                        metaData.getDatabaseProductVersion(),
                        metaData.getURL(),
                        metaData.getUserName()
                );
                log.info(statusBanner);
                System.out.println(statusBanner);
            } catch (Exception ex) {
                final String errorBanner = String.format(
                        "\n======================================================================\n" +
                        " [DATABASE CONNECTION FAILURE]\n" +
                        " Could not connect to MySQL Database.\n" +
                        " Error Details: %s\n" +
                        " Please check if MySQL server is running on localhost:3306\n" +
                        "======================================================================\n",
                        ex.getMessage()
                );
                log.error(errorBanner);
                System.err.println(errorBanner);
            }
        };
    }
}
