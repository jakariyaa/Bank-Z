package com.bankz.services;

import com.bankz.dao.CustomerDao;
import com.bankz.dao.EmployeeDao;
import com.bankz.models.Customer;
import com.bankz.models.Employee;
import com.bankz.util.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceImplTest {
    
    private TestCustomerDao customerDao;
    private TestEmployeeDao employeeDao;
    private AuthenticationServiceImpl authenticationService;
    
    @BeforeEach
    void setUp() {
        customerDao = new TestCustomerDao();
        employeeDao = new TestEmployeeDao();
        authenticationService = new AuthenticationServiceImpl(customerDao, employeeDao);
    }
    
    @Test
    void testAuthenticateCustomer_Success() {
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        String hashedPassword = PasswordUtils.hashPassword(password);
        Customer customer = new Customer(1, "John", "Doe", username, hashedPassword, null);
        customerDao.saveCustomer(customer);
        
        // Act
        Customer result = authenticationService.authenticateCustomer(username, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
        assertEquals(customer.getUsername(), result.getUsername());
    }
    
    @Test
    void testAuthenticateCustomer_WrongPassword() {
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        String wrongPassword = "wrongpassword";
        String hashedPassword = PasswordUtils.hashPassword(password);
        Customer customer = new Customer(1, "John", "Doe", username, hashedPassword, null);
        customerDao.saveCustomer(customer);
        
        // Act
        Customer result = authenticationService.authenticateCustomer(username, wrongPassword);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void testAuthenticateCustomer_UserNotFound() {
        // Arrange
        String username = "nonexistent";
        String password = "testpassword";
        
        // Act
        Customer result = authenticationService.authenticateCustomer(username, password);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void testRegisterCustomer_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe";
        String password = "testpassword";
        
        // Act
        Customer result = authenticationService.registerCustomer(firstName, lastName, username, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(username, result.getUsername());
        assertTrue(PasswordUtils.verifyPassword(password, result.getPasswordHash()));
    }
    
    @Test
    void testRegisterCustomer_UsernameExists() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "existinguser";
        String password = "testpassword";
        
        // Pre-register a customer with the same username
        customerDao.saveCustomer(new Customer(1, "Jane", "Smith", username, "hashed", null));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.registerCustomer(firstName, lastName, username, password);
        });
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
    
    // Simple test implementation of EmployeeDao for testing purposes
    private static class TestEmployeeDao implements EmployeeDao {
        private final List<Employee> employees = new ArrayList<>();
        
        @Override
        public Employee save(Employee entity) throws SQLException {
            employees.add(entity);
            return entity;
        }
        
        @Override
        public Employee findById(Integer id) throws SQLException {
            return employees.stream()
                    .filter(e -> e.getEmployeeId() == id)
                    .findFirst()
                    .orElse(null);
        }
        
        @Override
        public List<Employee> findAll() throws SQLException {
            return new ArrayList<>(employees);
        }
        
        @Override
        public boolean update(Employee entity) throws SQLException {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeId() == entity.getEmployeeId()) {
                    employees.set(i, entity);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean delete(Integer id) throws SQLException {
            return employees.removeIf(e -> e.getEmployeeId() == id);
        }
        
        @Override
        public Employee findByUsername(String username) throws SQLException {
            return employees.stream()
                    .filter(e -> e.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }
        
        @Override
        public boolean usernameExists(String username) throws SQLException {
            return employees.stream()
                    .anyMatch(e -> e.getUsername().equals(username));
        }
    }
}