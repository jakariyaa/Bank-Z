package com.bankz.dao;

import com.bankz.models.Customer;
import com.bankz.util.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoImplTest {
    
    private CustomerDao customerDao;
    
    @BeforeEach
    void setUp() throws SQLException {
        // Use an in-memory database for testing
        System.setProperty("db.url", "jdbc:sqlite:mem:bankz_test.db");
        DatabaseManager.initializeDatabase();
        customerDao = new CustomerDaoImpl();
        
        // Clear the database before each test
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM customers");
        }
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        // Clean up the database
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM customers");
        }
        DatabaseManager.closeConnection();
    }
    
    @Test
    void testSaveAndFindCustomer() throws SQLException {
        // Create a new customer
        Customer customer = new Customer("John", "Doe", "johndoe", "hashedpassword");
        
        // Save the customer
        Customer savedCustomer = customerDao.save(customer);
        
        // Verify the customer was saved with an ID
        assertNotNull(savedCustomer.getCustomerId());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
        assertEquals("johndoe", savedCustomer.getUsername());
        assertEquals("hashedpassword", savedCustomer.getPasswordHash());
        
        // Find the customer by ID
        Customer foundCustomer = customerDao.findById(savedCustomer.getCustomerId());
        
        // Verify the found customer matches the saved customer
        assertNotNull(foundCustomer);
        assertEquals(savedCustomer.getCustomerId(), foundCustomer.getCustomerId());
        assertEquals("John", foundCustomer.getFirstName());
        assertEquals("Doe", foundCustomer.getLastName());
        assertEquals("johndoe", foundCustomer.getUsername());
        assertEquals("hashedpassword", foundCustomer.getPasswordHash());
    }
    
    @Test
    void testFindByUsername() throws SQLException {
        // Create a new customer
        Customer customer = new Customer("Jane", "Smith", "janesmith", "hashedpassword2");
        
        // Save the customer
        customerDao.save(customer);
        
        // Find the customer by username
        Customer foundCustomer = customerDao.findByUsername("janesmith");
        
        // Verify the found customer matches the saved customer
        assertNotNull(foundCustomer);
        assertEquals("Jane", foundCustomer.getFirstName());
        assertEquals("Smith", foundCustomer.getLastName());
        assertEquals("janesmith", foundCustomer.getUsername());
        assertEquals("hashedpassword2", foundCustomer.getPasswordHash());
    }
    
    @Test
    void testUsernameExists() throws SQLException {
        // Create a new customer
        Customer customer = new Customer("Bob", "Johnson", "bobjohnson", "hashedpassword3");
        
        // Save the customer
        customerDao.save(customer);
        
        // Check if username exists
        assertTrue(customerDao.usernameExists("bobjohnson"));
        assertFalse(customerDao.usernameExists("nonexistentuser"));
    }
}