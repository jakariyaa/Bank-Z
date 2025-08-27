package com.bankz.controllers;

import com.bankz.dao.EmployeeDao;
import com.bankz.dao.EmployeeDaoImpl;
import com.bankz.models.Employee;
import com.bankz.services.AuthenticationService;
import com.bankz.services.AuthenticationServiceImpl;
import com.bankz.util.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeLoginIntegrationTest {

    private static EmployeeDao employeeDao;
    private static AuthenticationService authenticationService;

    @BeforeAll
    static void setUp() throws SQLException {
        // Use in-memory database for testing
        System.setProperty("db.url", "jdbc:sqlite:mem:bankz_test.db");
        
        // Initialize the database
        DatabaseManager.initializeDatabase();
        
        // Initialize DAOs and services
        employeeDao = new EmployeeDaoImpl();
        authenticationService = new AuthenticationServiceImpl(null, employeeDao);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Clean up database
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS employees");
            stmt.execute("DROP TABLE IF EXISTS customers");
            stmt.execute("DROP TABLE IF EXISTS accounts");
            stmt.execute("DROP TABLE IF EXISTS transactions");
        }
        
        // Reset system property
        System.clearProperty("db.url");
    }

    @Test
    void testEmployeeRegistrationAndLogin() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe";
        String password = "securepassword";
        String role = "Manager";

        // Act - Register employee
        Employee registeredEmployee = authenticationService.registerEmployee(firstName, lastName, username, password, role);

        // Assert - Registration successful
        assertNotNull(registeredEmployee);
        assertEquals(firstName, registeredEmployee.getFirstName());
        assertEquals(lastName, registeredEmployee.getLastName());
        assertEquals(username, registeredEmployee.getUsername());
        assertEquals(role, registeredEmployee.getRole());
        assertTrue(registeredEmployee.getEmployeeId() > 0);

        // Act - Login with registered employee
        Employee loggedInEmployee = authenticationService.authenticateEmployee(username, password);

        // Assert - Login successful
        assertNotNull(loggedInEmployee);
        assertEquals(registeredEmployee.getEmployeeId(), loggedInEmployee.getEmployeeId());
        assertEquals(firstName, loggedInEmployee.getFirstName());
        assertEquals(lastName, loggedInEmployee.getLastName());
        assertEquals(username, loggedInEmployee.getUsername());
        assertEquals(role, loggedInEmployee.getRole());
    }

    @Test
    void testEmployeeLoginWithInvalidCredentials() {
        // Arrange
        String username = "nonexistent";
        String password = "wrongpassword";

        // Act
        Employee employee = authenticationService.authenticateEmployee(username, password);

        // Assert
        assertNull(employee);
    }
}