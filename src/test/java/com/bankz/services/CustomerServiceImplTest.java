package com.bankz.services;

import com.bankz.dao.CustomerDao;
import com.bankz.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {
    
    private TestCustomerDao customerDao;
    private CustomerServiceImpl customerService;
    
    @BeforeEach
    void setUp() {
        customerDao = new TestCustomerDao();
        customerService = new CustomerServiceImpl(customerDao);
    }
    
    @Test
    void testCreateCustomer_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe";
        String password = "testpassword";
        
        // Act
        Customer result = customerService.createCustomer(firstName, lastName, username, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(username, result.getUsername());
        assertNotNull(result.getPasswordHash());
    }
    
    @Test
    void testCreateCustomer_UsernameExists() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "existinguser";
        String password = "testpassword";
        
        // Pre-register a customer with the same username
        customerDao.saveCustomer(new Customer(1, "Jane", "Smith", username, "hashed", null));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(firstName, lastName, username, password);
        });
    }
    
    @Test
    void testFindCustomerById() {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "johndoe", "hashed", null);
        customerDao.saveCustomer(customer);
        
        // Act
        Customer result = customerService.findCustomerById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
        assertEquals(customer.getUsername(), result.getUsername());
    }
    
    @Test
    void testFindCustomerByUsername() {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "johndoe", "hashed", null);
        customerDao.saveCustomer(customer);
        
        // Act
        Customer result = customerService.findCustomerByUsername("johndoe");
        
        // Assert
        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
        assertEquals(customer.getUsername(), result.getUsername());
    }
    
    @Test
    void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer(1, "John", "Doe", "johndoe", "hashed", null);
        Customer customer2 = new Customer(2, "Jane", "Smith", "janesmith", "hashed", null);
        customerDao.saveCustomer(customer1);
        customerDao.saveCustomer(customer2);
        
        // Act
        List<Customer> result = customerService.getAllCustomers();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));
    }
    
    // Simple test implementation of CustomerDao for testing purposes
    private static class TestCustomerDao implements CustomerDao {
        private final List<Customer> customers = new ArrayList<>();
        private int nextId = 1;
        
        public void saveCustomer(Customer customer) {
            customer.setCustomerId(nextId++);
            customers.add(customer);
        }
        
        @Override
        public Customer save(Customer entity) throws SQLException {
            entity.setCustomerId(nextId++);
            customers.add(entity);
            return entity;
        }
        
        @Override
        public Customer findById(Integer id) throws SQLException {
            return customers.stream()
                    .filter(c -> c.getCustomerId() == id)
                    .findFirst()
                    .orElse(null);
        }
        
        @Override
        public List<Customer> findAll() throws SQLException {
            return new ArrayList<>(customers);
        }
        
        @Override
        public boolean update(Customer entity) throws SQLException {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId() == entity.getCustomerId()) {
                    customers.set(i, entity);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean delete(Integer id) throws SQLException {
            return customers.removeIf(c -> c.getCustomerId() == id);
        }
        
        @Override
        public Customer findByUsername(String username) throws SQLException {
            return customers.stream()
                    .filter(c -> c.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }
        
        @Override
        public boolean usernameExists(String username) throws SQLException {
            return customers.stream()
                    .anyMatch(c -> c.getUsername().equals(username));
        }
    }
}