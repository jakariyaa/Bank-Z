package com.bankz.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private int customerId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private LocalDateTime dateOpened;
    private String status;
    
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.status = "ACTIVE";
    }
    
    public Account(int customerId, String accountNumber, String accountType) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO;
        this.dateOpened = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    public Account(int accountId, int customerId, String accountNumber, String accountType, 
                  BigDecimal balance, LocalDateTime dateOpened, String status) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.status = status;
    }
    
    // Getters and setters
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public LocalDateTime getDateOpened() {
        return dateOpened;
    }
    
    public void setDateOpened(LocalDateTime dateOpened) {
        this.dateOpened = dateOpened;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return accountType + " (" + accountNumber + ") - $" + balance;
    }
}