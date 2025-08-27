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

/**
 * Controller class for the create account dialog
 */
public class CreateAccountDialogController {
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private TextField initialDepositField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Customer customer;
    private AccountService accountService;
    private Runnable onAccountCreated;
    
    @FXML
    public void initialize() {
        // Initialize the account service
        AccountDao accountDao = new AccountDaoImpl();
        TransactionDao transactionDao = new TransactionDaoImpl();
        accountService = new AccountServiceImpl(accountDao, transactionDao);
        
        // Initialize account types
        accountTypeComboBox.getItems().addAll("Checking", "Savings");
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setOnAccountCreated(Runnable onAccountCreated) {
        this.onAccountCreated = onAccountCreated;
    }
    
    @FXML
    private void handleCreate(ActionEvent event) {
        String accountType = accountTypeComboBox.getValue();
        String initialDepositText = initialDepositField.getText();
        String description = descriptionField.getText();
        
        // Validate input
        if (accountType == null || accountType.isEmpty()) {
            messageLabel.setText("Please select an account type");
            return;
        }
        
        // Create the account
        Account newAccount = accountService.openAccount(customer.getCustomerId(), accountType);
        if (newAccount != null) {
            // If there's an initial deposit, process it
            if (initialDepositText != null && !initialDepositText.isEmpty()) {
                try {
                    BigDecimal initialDeposit = new BigDecimal(initialDepositText);
                    if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
                        String depositDescription = description.isEmpty() ? "Initial deposit" : description;
                        accountService.deposit(newAccount.getAccountId(), initialDeposit, depositDescription);
                    }
                } catch (NumberFormatException e) {
                    // Just ignore invalid deposit amounts
                    messageLabel.setText("Invalid deposit amount ignored");
                }
            }
            
            messageLabel.setText("Account created successfully!");
            
            // Clear the form
            accountTypeComboBox.getSelectionModel().clearSelection();
            initialDepositField.clear();
            descriptionField.clear();
            
            // Notify the parent controller
            if (onAccountCreated != null) {
                onAccountCreated.run();
            }
            
            // Close the dialog after a short delay
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> {
                        Stage stage = (Stage) createButton.getScene().getWindow();
                        stage.close();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            messageLabel.setText("Failed to create account. Please try again.");
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}