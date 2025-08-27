package com.bankz.services;

/**
 * Exception thrown when an account has insufficient funds for a transaction
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}