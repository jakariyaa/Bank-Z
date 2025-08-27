package com.bankz.controllers;

import com.bankz.App;
import com.bankz.dao.EmployeeDao;
import com.bankz.dao.EmployeeDaoImpl;
import com.bankz.models.Employee;
import com.bankz.services.AuthenticationService;
import com.bankz.services.AuthenticationServiceImpl;

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

public class EmployeeRegisterController {

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
    private TextField roleField;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label messageLabel;

    private AuthenticationService authenticationService;

    @FXML
    public void initialize() {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        authenticationService = new AuthenticationServiceImpl(null, employeeDao);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleField.getText().trim();

        // Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showMessage("Please fill in all fields", "error");
            return;
        }

        // Validate name fields
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            showMessage("First name and last name must contain only letters", "error");
            return;
        }

        // Validate username
        if (username.length() < 3) {
            showMessage("Username must be at least 3 characters long", "error");
            return;
        }

        // Validate password
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match", "error");
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters long", "error");
            return;
        }

        // Validate role
        if (!role.matches("[a-zA-Z ]+")) {
            showMessage("Role must contain only letters and spaces", "error");
            return;
        }

        try {
            Employee employee = authenticationService.registerEmployee(firstName, lastName, username, password, role);
            if (employee != null) {
                showMessage("Registration successful!", "success");
                // Add a small delay before navigating to login to allow user to see success message
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    javafx.application.Platform.runLater(this::navigateToLogin);
                }).start();
            } else {
                showMessage("Registration failed. Username may already exist.", "error");
            }
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), "error");
        } catch (Exception e) {
            showMessage("An unexpected error occurred during registration", "error");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        navigateToLogin();
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        // Clear previous style classes
        messageLabel.getStyleClass().removeAll("message-label", "message-label-error", "message-label-success");
        
        // Add appropriate style class based on message type
        switch (type) {
            case "error":
                messageLabel.getStyleClass().add("message-label-error");
                break;
            case "success":
                messageLabel.getStyleClass().add("message-label-success");
                break;
            default:
                messageLabel.getStyleClass().add("message-label");
                break;
        }
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root, App.LOGIN_WIDTH, App.LOGIN_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Bank-Z");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
