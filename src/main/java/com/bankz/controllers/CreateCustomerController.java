package com.bankz.controllers;

import com.bankz.models.Customer;
import com.bankz.services.AuthenticationService;
import com.bankz.services.AuthenticationServiceImpl;
import com.bankz.dao.CustomerDao;
import com.bankz.dao.CustomerDaoImpl;
import com.bankz.dao.EmployeeDao;
import com.bankz.dao.EmployeeDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller class for the create customer dialog
 */
public class CreateCustomerController {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private AuthenticationService authenticationService;
    private Runnable onCustomerCreated;
    
    @FXML
    public void initialize() {
        // Initialize the authentication service
        CustomerDao customerDao = new CustomerDaoImpl();
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        authenticationService = new AuthenticationServiceImpl(customerDao, employeeDao);
    }
    
    public void setOnCustomerCreated(Runnable onCustomerCreated) {
        this.onCustomerCreated = onCustomerCreated;
    }
    
    @FXML
    private void handleCreate(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }
        
        if (password.length() < 6) {
            messageLabel.setText("Password must be at least 6 characters long");
            return;
        }
        
        try {
            // Register the customer
            Customer customer = authenticationService.registerCustomer(firstName, lastName, username, password);
            if (customer != null) {
                messageLabel.setText("Customer created successfully!");
                // Clear the form
                firstNameField.clear();
                lastNameField.clear();
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
                
                // Notify the parent controller
                if (onCustomerCreated != null) {
                    onCustomerCreated.run();
                }
            } else {
                messageLabel.setText("Failed to create customer. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}