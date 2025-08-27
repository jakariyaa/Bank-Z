package com.bankz.dao;

import com.bankz.models.Employee;
import java.sql.SQLException;

/**
 * DAO interface for Employee entities
 */
public interface EmployeeDao extends Dao<Employee, Integer> {
    
    /**
     * Finds an employee by username
     * @param username The username to search for
     * @return The employee if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    Employee findByUsername(String username) throws SQLException;
    
    /**
     * Checks if a username already exists
     * @param username The username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean usernameExists(String username) throws SQLException;
}