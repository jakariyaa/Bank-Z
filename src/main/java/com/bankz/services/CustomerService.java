package com.bankz.services;

import com.bankz.models.Customer;
import java.util.List;

/**
 * Service interface for customer operations
 */
public interface CustomerService {
    
    /**
     * Creates a new customer
     * @param firstName The customer's first name
     * @param lastName The customer's last name
     * @param username The customer's username
     * @param password The customer's password
     * @return The created customer
     */
    Customer createCustomer(String firstName, String lastName, String username, String password);
    
    /**
     * Finds a customer by ID
     * @param customerId The ID of the customer
     * @return The customer if found, null otherwise
     */
    Customer findCustomerById(int customerId);
    
    /**
     * Finds a customer by username
     * @param username The username of the customer
     * @return The customer if found, null otherwise
     */
    Customer findCustomerByUsername(String username);
    
    /**
     * Gets all customers
     * @return A list of all customers
     */
    List<Customer> getAllCustomers();
    
    /**
     * Updates a customer's information
     * @param customer The customer with updated information
     * @return true if the update was successful, false otherwise
     */
    boolean updateCustomer(Customer customer);
    
    /**
     * Deletes a customer
     * @param customerId The ID of the customer to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteCustomer(int customerId);
}