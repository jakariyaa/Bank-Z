package com.bankz.services;

import com.bankz.dao.AccountDao;
import com.bankz.dao.TransactionDao;
import com.bankz.models.Account;
import com.bankz.models.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {
    
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    
    public AccountServiceImpl(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }
    
    @Override
    public Account openAccount(int customerId, String accountType) {
        try {
            String accountNumber = generateAccountNumber();
            
            Account account = new Account(customerId, accountNumber, accountType);
            return accountDao.save(account);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Account> getAccountsByCustomer(int customerId) {
        try {
            return accountDao.findByCustomerId(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Account deposit(int accountId, BigDecimal amount, String description) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            
            Account account = accountDao.findById(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account not found");
            }
            
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            accountDao.update(account);
            
            Transaction transaction = new Transaction(null, accountId, "DEPOSIT", amount, description);
            transactionDao.save(transaction);
            
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Account withdraw(int accountId, BigDecimal amount, String description) throws InsufficientFundsException {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }
            
            Account account = accountDao.findById(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account not found");
            }
            
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in account. Available balance: $" + account.getBalance());
            }
            
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            accountDao.update(account);
            
            Transaction transaction = new Transaction(accountId, null, "WITHDRAWAL", amount, description);
            transactionDao.save(transaction);
            
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean transfer(int sourceAccountId, int destinationAccountId, BigDecimal amount, String description) throws InsufficientFundsException {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }
            
            Account sourceAccount = accountDao.findById(sourceAccountId);
            Account destinationAccount = accountDao.findById(destinationAccountId);
            
            if (sourceAccount == null || destinationAccount == null) {
                throw new IllegalArgumentException("One or both accounts not found");
            }
            
            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in source account. Available balance: $" + sourceAccount.getBalance());
            }
            
            BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(amount);
            BigDecimal newDestinationBalance = destinationAccount.getBalance().add(amount);
            
            sourceAccount.setBalance(newSourceBalance);
            destinationAccount.setBalance(newDestinationBalance);
            
            accountDao.update(sourceAccount);
            accountDao.update(destinationAccount);
            
            Transaction transaction = new Transaction(sourceAccountId, destinationAccountId, "TRANSFER", amount, description);
            transactionDao.save(transaction);
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Transaction> getTransactionHistory(int accountId) {
        try {
            return transactionDao.findByAccountId(accountId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountDao.findByAccountNumber(accountNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean freezeAccount(int accountId) {
        try {
            Account account = accountDao.findById(accountId);
            if (account == null) {
                return false;
            }
            
            account.setStatus("FROZEN");
            return accountDao.update(account);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean closeAccount(int accountId) {
        try {
            Account account = accountDao.findById(accountId);
            if (account == null) {
                return false;
            }
            
            account.setStatus("CLOSED");
            return accountDao.update(account);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}