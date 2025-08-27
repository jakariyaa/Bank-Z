package com.bankz.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.bankz.App;
import com.bankz.dao.AccountDao;
import com.bankz.dao.AccountDaoImpl;
import com.bankz.dao.TransactionDao;
import com.bankz.dao.TransactionDaoImpl;
import com.bankz.models.Account;
import com.bankz.models.Customer;
import com.bankz.models.Transaction;
import com.bankz.services.AccountService;
import com.bankz.services.AccountServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerDashboardController {

    // Navigation buttons
    @FXML
    private Button dashboardButton;

    @FXML
    private Button accountsButton;

    @FXML
    private Button transferButton;

    @FXML
    private Button depositWithdrawButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private Button logoutButton;

    // Header
    @FXML
    private Label welcomeLabel;

    // Content areas
    @FXML
    private VBox dashboardOverview;

    @FXML
    private VBox accountsView;

    @FXML
    private VBox transactionsView;

    // Dashboard components
    @FXML
    private VBox accountsContainer;

    @FXML
    private VBox transactionsContainer;

    // Accounts view components
    @FXML
    private Button createAccountButton;

    @FXML
    private TableView<Account> accountsTable;

    @FXML
    private TableColumn<Account, String> accountNumberColumn;

    @FXML
    private TableColumn<Account, String> accountTypeColumn;

    @FXML
    private TableColumn<Account, BigDecimal> accountBalanceColumn;

    @FXML
    private TableColumn<Account, String> accountStatusColumn;

    // Transactions view components
    @FXML
    private ComboBox<Account> accountFilterComboBox;

    @FXML
    private Button refreshTransactionsButton;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> transactionDateColumn;

    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> transactionAmountColumn;

    @FXML
    private TableColumn<Transaction, String> transactionDescriptionColumn;

    private Customer customer;
    private AccountService accountService;
    private ObservableList<Account> accountsList;
    private ObservableList<Transaction> transactionsList;

    @FXML
    public void initialize() {
        // Initialize the account service
        AccountDao accountDao = new AccountDaoImpl();
        TransactionDao transactionDao = new TransactionDaoImpl();
        accountService = new AccountServiceImpl(accountDao, transactionDao);

        // Initialize table columns
        setupAccountsTable();
        setupTransactionsTable();

        // Initialize lists
        accountsList = FXCollections.observableArrayList();
        transactionsList = FXCollections.observableArrayList();

        // Set default view to dashboard
        showDashboard();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        welcomeLabel.setText("Welcome, " + customer.getFirstName() + " " + customer.getLastName());
        loadCustomerData();
    }

    private void setupAccountsTable() {
        accountNumberColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountNumber()));
        accountTypeColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountType()));
        accountBalanceColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBalance()));
        accountStatusColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
    }

    private void setupTransactionsTable() {
        transactionDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTimestamp().toString()));
        transactionTypeColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        transactionAmountColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
        transactionDescriptionColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
    }

    private void loadCustomerData() {
        try {
            // Load accounts
            List<Account> accounts = accountService.getAccountsByCustomer(customer.getCustomerId());
            accountsContainer.getChildren().clear();
            accountsList.clear();

            if (accounts != null) {
                accountsList.addAll(accounts);
                accountsTable.setItems(accountsList);
                accountFilterComboBox.setItems(accountsList);

                for (Account account : accounts) {
                    addAccountCard(account);
                }

                // Load recent transactions (from the first account if available)
                if (!accounts.isEmpty()) {
                    List<Transaction> transactions = accountService
                            .getTransactionHistory(accounts.get(0).getAccountId());
                    transactionsContainer.getChildren().clear();
                    transactionsList.clear();

                    if (transactions != null) {
                        transactionsList.addAll(transactions);
                        transactionsTable.setItems(transactionsList);

                        for (Transaction transaction : transactions) {
                            addTransactionItem(transaction);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading customer data: " + e.getMessage());
        }
    }

    private void addAccountCard(Account account) {
        VBox accountCard = new VBox(5);
        accountCard.getStyleClass().add("account-card");

        Label accountType = new Label(account.getAccountType());
        accountType.getStyleClass().add("account-type");

        Label accountNumber = new Label("Account #: " + account.getAccountNumber());
        accountNumber.getStyleClass().add("account-number");

        Label accountBalance = new Label("Balance: $" + account.getBalance());
        accountBalance.getStyleClass().add("account-balance");

        accountCard.getChildren().addAll(accountType, accountNumber, accountBalance);
        accountsContainer.getChildren().add(accountCard);
    }

    private void addTransactionItem(Transaction transaction) {
        javafx.scene.layout.HBox transactionItem = new javafx.scene.layout.HBox(10);
        transactionItem.getStyleClass().add("transaction-item");

        Label description = new Label(
                transaction.getDescription() != null ? transaction.getDescription() : transaction.getType());
        Label amount = new Label("$" + transaction.getAmount());

        switch (transaction.getType()) {
            case "DEPOSIT":
                amount.getStyleClass().add("transaction-amount-deposit");
                break;
            case "WITHDRAWAL":
                amount.getStyleClass().add("transaction-amount-withdrawal");
                break;
            case "TRANSFER":
                amount.getStyleClass().add("transaction-amount-transfer");
                break;
        }

        transactionItem.getChildren().addAll(description, amount);
        transactionsContainer.getChildren().add(transactionItem);
    }

    // Navigation handlers
    @FXML
    private void handleDashboard(ActionEvent event) {
        showDashboard();
    }

    @FXML
    private void handleAccounts(ActionEvent event) {
        showAccounts();
    }

    @FXML
    private void handleTransfer(ActionEvent event) {
        try {
            // Load the transfer funds dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/transfer_funds.fxml"));
            Parent root = loader.load();

            // Get the controller and set the customer
            TransferFundsController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setOnTransferSuccess(this::loadCustomerData); // Refresh data after transfer

            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle("Transfer Funds");
            stage.setScene(new Scene(root, 500, 700));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening transfer dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDepositWithdraw(ActionEvent event) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Deposit", "Deposit", "Withdraw");
        dialog.setTitle("Select Action");
        dialog.setHeaderText("Deposit or Withdraw");
        dialog.setContentText("Choose your action:");

        dialog.showAndWait().ifPresent(action -> {
            try {
                if ("Deposit".equals(action)) {
                    // Load the deposit dialog
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/deposit.fxml"));
                    Parent root = loader.load();

                    // Get the controller and set the customer
                    DepositController controller = loader.getController();
                    controller.setCustomer(customer);
                    controller.setOnDepositSuccess(this::loadCustomerData); // Refresh data after deposit

                    // Create a new stage for the dialog
                    Stage stage = new Stage();
                    stage.setTitle("Deposit Funds");
                    stage.setScene(new Scene(root, 700, 500));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                } else {
                    // Load the withdraw dialog
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/withdraw.fxml"));
                    Parent root = loader.load();

                    // Get the controller and set the customer
                    WithdrawController controller = loader.getController();
                    controller.setCustomer(customer);
                    controller.setOnWithdrawSuccess(this::loadCustomerData); // Refresh data after withdrawal

                    // Create a new stage for the dialog
                    Stage stage = new Stage();
                    stage.setTitle("Withdraw Funds");
                    stage.setScene(new Scene(root, 700, 550));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error opening dialog: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleTransactions(ActionEvent event) {
        showTransactions();
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        try {
            // Load the create account dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/create_account_dialog.fxml"));
            Parent root = loader.load();

            // Get the controller and set the customer
            CreateAccountDialogController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setOnAccountCreated(this::loadCustomerData); // Refresh data after account creation

            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root, 700, 550));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening create account dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefreshTransactions(ActionEvent event) {
        Account selectedAccount = accountFilterComboBox.getValue();
        if (selectedAccount != null) {
            try {
                List<Transaction> transactions = accountService.getTransactionHistory(selectedAccount.getAccountId());
                transactionsList.clear();
                if (transactions != null) {
                    transactionsList.addAll(transactions);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error refreshing transactions: " + e.getMessage());
            }
        }
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

    // View switching methods
    private void showDashboard() {
        dashboardOverview.setVisible(true);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
    }

    private void showAccounts() {
        dashboardOverview.setVisible(false);
        accountsView.setVisible(true);
        transactionsView.setVisible(false);
        loadCustomerData(); // Refresh accounts data
    }

    private void showTransactions() {
        dashboardOverview.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(true);
        loadCustomerData(); // Refresh transactions data
    }

    private void showError(String message) {
        // Show error in a proper alert dialog
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}