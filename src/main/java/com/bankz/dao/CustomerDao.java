package com.bankz.dao;

import com.bankz.models.Customer;
import java.sql.SQLException;

/**
 * DAO interface for Customer entities
 */
public interface CustomerDao extends Dao<Customer, Integer> {
    
    /**
     * Finds a customer by username
     * @param username The username to search for
     * @return The customer if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    Customer findByUsername(String username) throws SQLException;
    
    /**
     * Checks if a username already exists
     * @param username The username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean usernameExists(String username) throws SQLException;
}