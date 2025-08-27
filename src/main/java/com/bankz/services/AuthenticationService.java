package com.bankz.services;

import com.bankz.models.Customer;
import com.bankz.models.Employee;

/**
 * Service interface for authentication operations
 */
public interface AuthenticationService {

    /**
     * Authenticates a customer
     * 
     * @param username The customer's username
     * @param password The customer's password
     * @return The authenticated customer, or null if authentication failed
     */
    Customer authenticateCustomer(String username, String password);

    /**
     * Authenticates an employee
     * 
     * @param username The employee's username
     * @param password The employee's password
     * @return The authenticated employee, or null if authentication failed
     */
    Employee authenticateEmployee(String username, String password);

    /**
     * Registers a new customer
     * 
     * @param firstName The customer's first name
     * @param lastName  The customer's last name
     * @param username  The customer's username
     * @param password  The customer's password
     * @return The registered customer
     */
    Customer registerCustomer(String firstName, String lastName, String username, String password);

    /**
     * Registers a new employee
     * 
     * @param firstName The employee's first name
     * @param lastName  The employee's last name
     * @param username  The employee's username
     * @param password  The employee's password
     * @param role      The employee's role
     * @return The registered employee
     */
    Employee registerEmployee(String firstName, String lastName, String username, String password, String role);
}