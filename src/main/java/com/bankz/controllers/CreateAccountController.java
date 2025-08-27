package com.bankz.controllers;

import com.bankz.models.Account;
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
public class CreateAccountController {
    
    @FXML
    private TextField customerIdField;
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private TextField initialBalanceField;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
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
    
    public void setOnAccountCreated(Runnable onAccountCreated) {
        this.onAccountCreated = onAccountCreated;
    }
    
    public void setCustomerId(int customerId) {
        customerIdField.setText(String.valueOf(customerId));
        // Disable the field so the user cannot change it
        customerIdField.setDisable(true);
    }
    
    @FXML
    private void handleCreate(ActionEvent event) {
        String customerIdText = customerIdField.getText();
        String accountType = accountTypeComboBox.getValue();
        String initialBalanceText = initialBalanceField.getText();
        
        // Validate input
        if (customerIdText.isEmpty()) {
            messageLabel.setText("Please enter a customer ID");
            return;
        }
        
        if (accountType == null || accountType.isEmpty()) {
            messageLabel.setText("Please select an account type");
            return;
        }
        
        int customerId;
        try {
            customerId = Integer.parseInt(customerIdText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid customer ID");
            return;
        }
        
        BigDecimal initialBalance = BigDecimal.ZERO;
        if (!initialBalanceText.isEmpty()) {
            try {
                initialBalance = new BigDecimal(initialBalanceText);
                if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                    messageLabel.setText("Initial balance cannot be negative");
                    return;
                }
            } catch (NumberFormatException e) {
                messageLabel.setText("Please enter a valid initial balance");
                return;
            }
        }
        
        // Create the account
        Account account = accountService.openAccount(customerId, accountType);
        if (account != null) {
            // If there's an initial balance, deposit it
            if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
                accountService.deposit(account.getAccountId(), initialBalance, "Initial deposit");
            }
            
            messageLabel.setText("Account created successfully!");
            // Clear the form
            customerIdField.clear();
            accountTypeComboBox.getSelectionModel().clearSelection();
            initialBalanceField.clear();
            
            // Notify the parent controller
            if (onAccountCreated != null) {
                onAccountCreated.run();
            }
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