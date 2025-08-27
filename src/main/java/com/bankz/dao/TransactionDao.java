package com.bankz.dao;

import com.bankz.models.Transaction;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for Transaction entities
 */
public interface TransactionDao extends Dao<Transaction, Integer> {
    
    /**
     * Finds all transactions for a specific account
     * @param accountId The ID of the account
     * @return A list of transactions for the account
     * @throws SQLException if a database access error occurs
     */
    List<Transaction> findByAccountId(int accountId) throws SQLException;
    
    /**
     * Finds all transactions between two accounts
     * @param sourceAccountId The ID of the source account
     * @param destinationAccountId The ID of the destination account
     * @return A list of transactions between the accounts
     * @throws SQLException if a database access error occurs
     */
    List<Transaction> findByAccounts(int sourceAccountId, int destinationAccountId) throws SQLException;
    
    /**
     * Finds all transactions in the system
     * @return A list of all transactions
     * @throws SQLException if a database access error occurs
     */
    List<Transaction> findAll() throws SQLException;
}