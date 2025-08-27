package com.bankz.dao;

import com.bankz.models.Account;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for Account entities
 */
public interface AccountDao extends Dao<Account, Integer> {
    
    /**
     * Finds accounts by customer ID
     * @param customerId The ID of the customer
     * @return A list of accounts belonging to the customer
     * @throws SQLException if a database access error occurs
     */
    List<Account> findByCustomerId(int customerId) throws SQLException;
    
    /**
     * Finds an account by its account number
     * @param accountNumber The account number
     * @return The account if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    Account findByAccountNumber(String accountNumber) throws SQLException;
    
    /**
     * Updates the balance of an account
     * @param accountId The ID of the account
     * @param newBalance The new balance
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean updateBalance(int accountId, double newBalance) throws SQLException;
}