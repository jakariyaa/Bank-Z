package com.bankz.controllers;

import com.bankz.models.Account;
import com.bankz.models.Customer;
import com.bankz.services.AccountService;
import com.bankz.services.AccountServiceImpl;
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
 * Controller class for the deposit dialog
 */
public class DepositController {
    
    @FXML
    private ComboBox<Account> accountComboBox;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Button depositButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Customer customer;
    private AccountService accountService;
    private Runnable onDepositSuccess;
    
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
    
    public void setOnDepositSuccess(Runnable onDepositSuccess) {
        this.onDepositSuccess = onDepositSuccess;
    }
    
    private void loadCustomerAccounts() {
        try {
            accountComboBox.getItems().clear();
            
            List<Account> accounts = accountService.getAccountsByCustomer(customer.getCustomerId());
            if (accounts != null && !accounts.isEmpty()) {
                accountComboBox.getItems().addAll(accounts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show error in the message label
            messageLabel.setText("Error loading accounts: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeposit(ActionEvent event) {
        Account account = accountComboBox.getValue();
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        
        // Validate input
        if (account == null) {
            messageLabel.setText("Please select an account");
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
        
        // Perform the deposit
        Account updatedAccount = accountService.deposit(
                account.getAccountId(), 
                amount, 
                description.isEmpty() ? "Deposit" : description);
        
        if (updatedAccount != null) {
            messageLabel.setText("Deposit successful!");
            // Clear the form
            accountComboBox.getSelectionModel().clearSelection();
            amountField.clear();
            descriptionField.clear();
            
            // Reload accounts to reflect updated balance
            loadCustomerAccounts();
            
            // Notify the parent controller
            if (onDepositSuccess != null) {
                onDepositSuccess.run();
            }
        } else {
            messageLabel.setText("Deposit failed. Please try again.");
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}