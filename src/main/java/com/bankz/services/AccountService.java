package com.bankz.services;

import com.bankz.models.Account;
import com.bankz.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for account operations
 */
public interface AccountService {
    
    /**
     * Opens a new account for a customer
     * @param customerId The ID of the customer
     * @param accountType The type of account (e.g., "Checking", "Savings")
     * @return The newly created account
     */
    Account openAccount(int customerId, String accountType);
    
    /**
     * Finds all accounts for a specific customer
     * @param customerId The ID of the customer
     * @return A list of accounts belonging to the customer
     */
    List<Account> getAccountsByCustomer(int customerId);
    
    /**
     * Deposits money into an account
     * @param accountId The ID of the account
     * @param amount The amount to deposit
     * @param description Optional description of the deposit
     * @return The updated account
     */
    Account deposit(int accountId, BigDecimal amount, String description);
    
    /**
     * Withdraws money from an account
     * @param accountId The ID of the account
     * @param amount The amount to withdraw
     * @param description Optional description of the withdrawal
     * @return The updated account
     * @throws InsufficientFundsException if the account has insufficient funds
     */
    Account withdraw(int accountId, BigDecimal amount, String description) throws InsufficientFundsException;
    
    /**
     * Transfers money between accounts
     * @param sourceAccountId The ID of the source account
     * @param destinationAccountId The ID of the destination account
     * @param amount The amount to transfer
     * @param description Optional description of the transfer
     * @return true if the transfer was successful, false otherwise
     * @throws InsufficientFundsException if the source account has insufficient funds
     */
    boolean transfer(int sourceAccountId, int destinationAccountId, BigDecimal amount, String description) throws InsufficientFundsException;
    
    /**
     * Gets transaction history for an account
     * @param accountId The ID of the account
     * @return A list of transactions for the account
     */
    List<Transaction> getTransactionHistory(int accountId);
    
    /**
     * Gets an account by its account number
     * @param accountNumber The account number
     * @return The account if found, null otherwise
     */
    Account getAccountByAccountNumber(String accountNumber);
    
    /**
     * Freezes an account
     * @param accountId The ID of the account to freeze
     * @return true if the account was frozen successfully, false otherwise
     */
    boolean freezeAccount(int accountId);
    
    /**
     * Closes an account
     * @param accountId The ID of the account to close
     * @return true if the account was closed successfully, false otherwise
     */
    boolean closeAccount(int accountId);
}