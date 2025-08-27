package com.bankz.controllers;

import java.io.IOException;

import com.bankz.App;
import com.bankz.dao.CustomerDao;
import com.bankz.dao.CustomerDaoImpl;
import com.bankz.dao.EmployeeDao;
import com.bankz.dao.EmployeeDaoImpl;
import com.bankz.models.Customer;
import com.bankz.services.AuthenticationService;
import com.bankz.services.AuthenticationServiceImpl;
import com.bankz.services.CustomerService;
import com.bankz.services.CustomerServiceImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the registration screen
 */
public class RegisterController {

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
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label messageLabel;

    private AuthenticationService authenticationService;
    private CustomerService customerService;

    @FXML
    public void initialize() {
        // Initialize the services
        CustomerDao customerDao = new CustomerDaoImpl();
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        authenticationService = new AuthenticationServiceImpl(customerDao, employeeDao);
        customerService = new CustomerServiceImpl(customerDao);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
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
                messageLabel.setText("Registration successful!");
                // Navigate to customer dashboard
                navigateToCustomerDashboard(customer);
            } else {
                messageLabel.setText("Registration failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) cancelButton.getScene().getWindow();

            // Create the scene with the login screen
            Scene scene = new Scene(root, App.LOGIN_WIDTH, App.LOGIN_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            // Set the scene
            stage.setScene(scene);
            stage.setTitle("Bank-Z");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToCustomerDashboard(Customer customer) {
        try {
            // Load the customer dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/customer_dashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and set the customer
            CustomerDashboardController controller = loader.getController();
            controller.setCustomer(customer);

            // Get the current stage
            Stage stage = (Stage) registerButton.getScene().getWindow();

            // Create the scene with the customer dashboard
            Scene scene = new Scene(root, App.DASHBOARD_WIDTH, App.DASHBOARD_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            // Set the scene
            stage.setScene(scene);
            stage.setTitle("Bank-Z - Customer Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading customer dashboard");
        }
    }
}