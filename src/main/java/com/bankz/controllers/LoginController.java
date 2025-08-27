package com.bankz.controllers;

import java.io.IOException;

import com.bankz.App;
import com.bankz.dao.CustomerDao;
import com.bankz.dao.CustomerDaoImpl;
import com.bankz.dao.EmployeeDao;
import com.bankz.dao.EmployeeDaoImpl;
import com.bankz.models.Customer;
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

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button employeeRegisterButton;

    @FXML
    private Label messageLabel;

    private AuthenticationService authenticationService;

    @FXML
    public void initialize() {
        CustomerDao customerDao = new CustomerDaoImpl();
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        authenticationService = new AuthenticationServiceImpl(customerDao, employeeDao);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", "error");
            return;
        }

        Customer customer = authenticationService.authenticateCustomer(username, password);
        if (customer != null) {
            showMessage("Login successful! Redirecting...", "success");
            navigateToCustomerDashboard(customer);
            return;
        }

        Employee employee = authenticationService.authenticateEmployee(username, password);
        if (employee != null) {
            showMessage("Login successful! Redirecting...", "success");
            navigateToEmployeeDashboard(employee);
            return;
        }

        showMessage("Invalid username or password", "error");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root, App.LOGIN_HEIGHT, App.LOGIN_WIDTH);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Bank-Z - Register");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading registration screen", "error");
        }
    }

    @FXML
    private void handleEmployeeRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/employee_register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) employeeRegisterButton.getScene().getWindow();

            Scene scene = new Scene(root, App.LOGIN_WIDTH, App.LOGIN_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Bank-Z - Employee Register");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading employee registration screen", "error");
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("message-label", "message-label-error", "message-label-success");

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

    private void navigateToCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/customer_dashboard.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setCustomer(customer);

            Stage stage = (Stage) loginButton.getScene().getWindow();

            Scene scene = new Scene(root, App.DASHBOARD_WIDTH, App.DASHBOARD_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Bank-Z - Customer Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading customer dashboard", "error");
        }
    }

    private void navigateToEmployeeDashboard(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/employee_dashboard.fxml"));
            Parent root = loader.load();

            EmployeeDashboardController controller = loader.getController();
            controller.setEmployee(employee);

            Stage stage = (Stage) loginButton.getScene().getWindow();

            Scene scene = new Scene(root, App.DASHBOARD_WIDTH, App.DASHBOARD_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Bank-Z - Employee Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading employee dashboard", "error");
        }
    }
}