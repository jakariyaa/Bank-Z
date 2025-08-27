package com.bankz.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private Integer sourceAccountId;  // Can be null for deposits
    private Integer destinationAccountId;  // Can be null for withdrawals
    private String type;  // DEPOSIT, WITHDRAWAL, TRANSFER
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
    
    // Constructors
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Transaction(Integer sourceAccountId, Integer destinationAccountId, String type, 
                      BigDecimal amount, String description) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    public Transaction(int transactionId, Integer sourceAccountId, Integer destinationAccountId, 
                      String type, BigDecimal amount, LocalDateTime timestamp, String description) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }
    
    // Getters and setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public Integer getSourceAccountId() {
        return sourceAccountId;
    }
    
    public void setSourceAccountId(Integer sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }
    
    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }
    
    public void setDestinationAccountId(Integer destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}