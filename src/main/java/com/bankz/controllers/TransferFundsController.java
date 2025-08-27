package com.bankz.controllers;

import com.bankz.models.Account;
import com.bankz.models.Customer;
import com.bankz.services.AccountService;
import com.bankz.services.AccountServiceImpl;
import com.bankz.services.InsufficientFundsException;
import com.bankz.dao.AccountDao;
import com.bankz.dao.AccountDaoImpl;
import com.bankz.dao.TransactionDao;
import com.bankz.dao.TransactionDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller class for the transfer funds dialog
 */
public class TransferFundsController {
    
    @FXML
    private ComboBox<Account> fromAccountComboBox;
    
    @FXML
    private TextField toAccountNumberField;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Button transferButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Customer customer;
    private AccountService accountService;
    private Runnable onTransferSuccess;
    
    @FXML
    public void initialize() {
        // Initialize the account service
        AccountDao accountDao = new AccountDaoImpl();
        TransactionDao transactionDao = new TransactionDaoImpl();
        accountService = new AccountServiceImpl(accountDao, transactionDao);
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
        loadCustomerAccounts();
    }
    
    public void setOnTransferSuccess(Runnable onTransferSuccess) {
        this.onTransferSuccess = onTransferSuccess;
    }
    
    private void loadCustomerAccounts() {
        try {
            fromAccountComboBox.getItems().clear();
            
            List<Account> accounts = accountService.getAccountsByCustomer(customer.getCustomerId());
            if (accounts != null && !accounts.isEmpty()) {
                fromAccountComboBox.getItems().addAll(accounts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show error in the message label
            messageLabel.setText("Error loading accounts: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleTransfer(ActionEvent event) {
        Account fromAccount = fromAccountComboBox.getValue();
        String toAccountNumber = toAccountNumberField.getText();
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        
        // Validate input
        if (fromAccount == null) {
            messageLabel.setText("Please select a source account");
            return;
        }
        
        if (toAccountNumber.isEmpty()) {
            messageLabel.setText("Please enter a destination account number");
            return;
        }
        
        if (amountText.isEmpty()) {
            messageLabel.setText("Please enter an amount");
            return;
        }
        
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                messageLabel.setText("Amount must be positive");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid amount");
            return;
        }
        
        // Perform the transfer
        try {
            // First, we need to find the destination account by its account number
            Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);
            if (toAccount == null) {
                messageLabel.setText("Destination account not found");
                return;
            }
            
            // Check if source and destination accounts are the same
            if (fromAccount.getAccountId() == toAccount.getAccountId()) {
                messageLabel.setText("Source and destination accounts must be different");
                return;
            }
            
            boolean success = accountService.transfer(
                    fromAccount.getAccountId(), 
                    toAccount.getAccountId(), 
                    amount, 
                    description.isEmpty() ? "Transfer" : description);
            
            if (success) {
                messageLabel.setText("Transfer successful!");
                // Clear the form
                fromAccountComboBox.getSelectionModel().clearSelection();
                toAccountNumberField.clear();
                amountField.clear();
                descriptionField.clear();
                
                // Reload accounts to reflect updated balances
                loadCustomerAccounts();
                
                // Notify the parent controller
                if (onTransferSuccess != null) {
                    onTransferSuccess.run();
                }
            } else {
                messageLabel.setText("Transfer failed. Please try again.");
            }
        } catch (InsufficientFundsException e) {
            messageLabel.setText(e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}