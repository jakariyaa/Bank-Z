package com.bankz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:bankz.db";
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbUrl = System.getProperty("db.url", DEFAULT_DB_URL);
            connection = DriverManager.getConnection(dbUrl);
        }
        return connection;
    }
    
    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    date_created TEXT NOT NULL
                );
                """;
            stmt.execute(createCustomersTable);
            
            String createEmployeesTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL,
                    date_hired TEXT NOT NULL
                );
                """;
            stmt.execute(createEmployeesTable);
            
            String createAccountsTable = """
                CREATE TABLE IF NOT EXISTS accounts (
                    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    account_number TEXT NOT NULL UNIQUE,
                    account_type TEXT NOT NULL,
                    balance REAL NOT NULL DEFAULT 0.0,
                    date_opened TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'ACTIVE',
                    FOREIGN KEY(customer_id) REFERENCES customers(customer_id)
                );
                """;
            stmt.execute(createAccountsTable);
            
            String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    source_account_id INTEGER,
                    destination_account_id INTEGER,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    timestamp TEXT NOT NULL,
                    description TEXT,
                    FOREIGN KEY(source_account_id) REFERENCES accounts(account_id),
                    FOREIGN KEY(destination_account_id) REFERENCES accounts(account_id)
                );
                """;
            stmt.execute(createTransactionsTable);
        }
    }
    
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}