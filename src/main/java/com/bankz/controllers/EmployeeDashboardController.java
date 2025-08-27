package com.bankz.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.bankz.App;
import com.bankz.dao.AccountDao;
import com.bankz.dao.AccountDaoImpl;
import com.bankz.dao.CustomerDao;
import com.bankz.dao.CustomerDaoImpl;
import com.bankz.dao.TransactionDao;
import com.bankz.dao.TransactionDaoImpl;
import com.bankz.models.Account;
import com.bankz.models.Customer;
import com.bankz.models.Employee;
import com.bankz.models.Transaction;
import com.bankz.services.AccountService;
import com.bankz.services.AccountServiceImpl;
import com.bankz.services.CustomerService;
import com.bankz.services.CustomerServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for the employee dashboard
 */
public class EmployeeDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createCustomerButton;

    @FXML
    private Button searchCustomerButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button searchAccountButton;

    @FXML
    private Button freezeAccountButton;

    @FXML
    private Button closeAccountButton;

    @FXML
    private Button viewTransactionsButton;

    // Customer Management Tab
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerUsernameColumn;
    @FXML
    private TableColumn<Customer, String> customerDateCreatedColumn;

    // Account Management Tab
    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableColumn<Account, Integer> accountIdColumn;
    @FXML
    private TableColumn<Account, String> accountNumberColumn;
    @FXML
    private TableColumn<Account, String> accountTypeColumn;
    @FXML
    private TableColumn<Account, String> accountBalanceColumn;
    @FXML
    private TableColumn<Account, String> accountStatusColumn;

    // Transaction Viewer Tab
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transaction, Integer> transactionSourceColumn;
    @FXML
    private TableColumn<Transaction, Integer> transactionDestinationColumn;
    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;
    @FXML
    private TableColumn<Transaction, String> transactionAmountColumn;
    @FXML
    private TableColumn<Transaction, String> transactionDateColumn;

    private Employee employee;
    private CustomerService customerService;
    private AccountService accountService;
    private CustomerDao customerDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    @FXML
    public void initialize() {
        // Initialize services and DAOs
        customerDao = new CustomerDaoImpl();
        accountDao = new AccountDaoImpl();
        transactionDao = new TransactionDaoImpl();
        customerService = new CustomerServiceImpl(customerDao);
        accountService = new AccountServiceImpl(accountDao, transactionDao);

        // Initialize table columns
        initializeCustomerTable();
        initializeAccountTable();
        initializeTransactionTable();

        // Load initial data
        loadCustomers();
        loadAccounts();
        loadTransactions();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        welcomeLabel.setText(
                "Welcome, " + employee.getFirstName() + " " + employee.getLastName() + " (" + employee.getRole() + ")");
    }

    private void initializeCustomerTable() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        customerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        customerDateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
    }

    private void initializeAccountTable() {
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        accountBalanceColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                "$" + cellData.getValue().getBalance()));
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void initializeTransactionTable() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionSourceColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAccountId"));
        transactionDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAccountId"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        transactionAmountColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                "$" + cellData.getValue().getAmount()));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(customers);
            customersTable.setItems(customerObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading customers: " + e.getMessage());
        }
    }

    private void loadAccounts() {
        try {
            List<Account> accounts = accountDao.findAll();
            ObservableList<Account> accountObservableList = FXCollections.observableArrayList(accounts);
            accountsTable.setItems(accountObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading accounts: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionDao.findAll();
            ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList(transactions);
            transactionsTable.setItems(transactionObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading transactions: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateCustomer(ActionEvent event) {
        try {
            // Load the create customer dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/create_customer.fxml"));
            Parent root = loader.load();

            // Get the controller
            CreateCustomerController controller = loader.getController();
            controller.setOnCustomerCreated(() -> {
                // Refresh customer data
                loadCustomers();
                showSuccess("Customer created successfully");
            });

            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle("Create Customer");
            stage.setScene(new Scene(root, 700, 500));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening create customer dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchCustomer(ActionEvent event) {
        // Show a dialog to get customer ID or username
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Customer");
        dialog.setHeaderText("Search Customer");
        dialog.setContentText("Enter customer ID or username:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.isEmpty()) {
                try {
                    Customer customer = null;
                    if (input.matches("\\d+")) {
                        // Input is numeric, treat as customer ID
                        int customerId = Integer.parseInt(input);
                        customer = customerService.findCustomerById(customerId);
                    } else {
                        // Treat as username
                        customer = customerService.findCustomerByUsername(input);
                    }

                    if (customer != null) {
                        // Show customer details in an alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Customer Found");
                        alert.setHeaderText(null);
                        alert.setContentText(
                                "Customer ID: " + customer.getCustomerId() + "\n" +
                                "Name: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                                "Username: " + customer.getUsername() + "\n" +
                                "Date Created: " + customer.getDateCreated());
                        alert.showAndWait();
                    } else {
                        showError("Customer not found");
                    }
                } catch (NumberFormatException e) {
                    showError("Please enter a valid customer ID");
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error searching for customer: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        // First, we need to select a customer for the account
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Account");
        dialog.setHeaderText("Create Account");
        dialog.setContentText("Enter customer ID for the new account:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(input);
                    Customer customer = customerService.findCustomerById(customerId);

                    if (customer != null) {
                        try {
                            // Load the create account dialog
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/create_account.fxml"));
                            Parent root = loader.load();

                            // Get the controller and set the customer ID
                            CreateAccountController controller = loader.getController();
                            controller.setCustomerId(customerId);
                            controller.setOnAccountCreated(() -> {
                                // Refresh account data
                                loadAccounts();
                                showSuccess("Account created successfully");
                            });

                            // Create a new stage for the dialog
                            Stage stage = new Stage();
                            stage.setTitle("Create Account for " + customer.getFirstName() + " " + customer.getLastName());
                            stage.setScene(new Scene(root, 700, 500));
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Error opening create account dialog: " + e.getMessage());
                        }
                    } else {
                        showError("Customer not found");
                    }
                } catch (NumberFormatException e) {
                    showError("Please enter a valid customer ID");
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error creating account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleSearchAccount(ActionEvent event) {
        // Show a dialog to get account ID or number
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Account");
        dialog.setHeaderText("Search Account");
        dialog.setContentText("Enter account ID or account number:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.isEmpty()) {
                try {
                    Account account = null;
                    if (input.matches("\\d+")) {
                        // Input is numeric, treat as account ID
                        int accountId = Integer.parseInt(input);
                        account = accountDao.findById(accountId);
                    } else {
                        // Treat as account number
                        account = accountDao.findByAccountNumber(input);
                    }

                    if (account != null) {
                        // Show account details in an alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Account Found");
                        alert.setHeaderText(null);
                        alert.setContentText(
                                "Account ID: " + account.getAccountId() + "\n" +
                                "Account Number: " + account.getAccountNumber() + "\n" +
                                "Account Type: " + account.getAccountType() + "\n" +
                                "Balance: $" + account.getBalance() + "\n" +
                                "Status: " + account.getStatus() + "\n" +
                                "Date Opened: " + account.getDateOpened());
                        alert.showAndWait();
                    } else {
                        showError("Account not found");
                    }
                } catch (NumberFormatException e) {
                    showError("Please enter a valid account ID");
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error searching for account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleFreezeAccount(ActionEvent event) {
        // Show a dialog to get account ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Freeze Account");
        dialog.setHeaderText("Freeze Account");
        dialog.setContentText("Enter account ID to freeze:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.isEmpty()) {
                try {
                    int accountId = Integer.parseInt(input);
                    Account account = accountDao.findById(accountId);

                    if (account != null) {
                        // Confirm before freezing
                        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmAlert.setTitle("Confirm Freeze");
                        confirmAlert.setHeaderText(null);
                        confirmAlert.setContentText(
                                "Are you sure you want to freeze account #" + accountId + 
                                " (" + account.getAccountNumber() + ")?");

                        confirmAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    if (accountService.freezeAccount(accountId)) {
                                        // Refresh account data
                                        loadAccounts();
                                        showSuccess("Account frozen successfully");
                                    } else {
                                        showError("Failed to freeze account");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showError("Error freezing account: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        showError("Account not found");
                    }
                } catch (NumberFormatException e) {
                    showError("Please enter a valid account ID");
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error freezing account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleCloseAccount(ActionEvent event) {
        // Show a dialog to get account ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Close Account");
        dialog.setHeaderText("Close Account");
        dialog.setContentText("Enter account ID to close:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.isEmpty()) {
                try {
                    int accountId = Integer.parseInt(input);
                    Account account = accountDao.findById(accountId);

                    if (account != null) {
                        // Confirm before closing
                        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmAlert.setTitle("Confirm Close");
                        confirmAlert.setHeaderText(null);
                        confirmAlert.setContentText(
                                "Are you sure you want to close account #" + accountId + 
                                " (" + account.getAccountNumber() + ")?");

                        confirmAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    if (accountService.closeAccount(accountId)) {
                                        // Refresh account data
                                        loadAccounts();
                                        showSuccess("Account closed successfully");
                                    } else {
                                        showError("Failed to close account");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showError("Error closing account: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        showError("Account not found");
                    }
                } catch (NumberFormatException e) {
                    showError("Please enter a valid account ID");
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error closing account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleViewTransactions(ActionEvent event) {
        // Refresh transactions table
        loadTransactions();
        showSuccess("Transactions refreshed");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();

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

    private void showError(String message) {
        // Show error in a proper alert dialog
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        // Show success message in an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}