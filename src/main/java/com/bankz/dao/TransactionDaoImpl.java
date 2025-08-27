package com.bankz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bankz.models.Transaction;
import com.bankz.util.DatabaseManager;


public class TransactionDaoImpl implements TransactionDao {

    @Override
    public Transaction save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (source_account_id, destination_account_id, type, amount, timestamp, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setObject(1, transaction.getSourceAccountId(), Types.INTEGER);
            stmt.setObject(2, transaction.getDestinationAccountId(), Types.INTEGER);
            stmt.setString(3, transaction.getType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getTimestamp().toString());
            stmt.setString(6, transaction.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }

        return transaction;
    }

    @Override
    public Transaction findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }

        return transactions;
    }

    @Override
    public boolean update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET source_account_id = ?, destination_account_id = ?, type = ?, amount = ?, timestamp = ?, description = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, transaction.getSourceAccountId(), Types.INTEGER);
            stmt.setObject(2, transaction.getDestinationAccountId(), Types.INTEGER);
            stmt.setString(3, transaction.getType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getTimestamp().toString());
            stmt.setString(6, transaction.getDescription());
            stmt.setInt(7, transaction.getTransactionId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE source_account_id = ? OR destination_account_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> findByAccounts(int sourceAccountId, int destinationAccountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE (source_account_id = ? AND destination_account_id = ?) OR (source_account_id = ? AND destination_account_id = ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sourceAccountId);
            stmt.setInt(2, destinationAccountId);
            stmt.setInt(3, destinationAccountId);
            stmt.setInt(4, sourceAccountId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }

        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));

        // Handle source account ID (can be null)
        Object sourceAccountIdObj = rs.getObject("source_account_id");
        if (sourceAccountIdObj != null) {
            if (sourceAccountIdObj instanceof Integer) {
                transaction.setSourceAccountId((Integer) sourceAccountIdObj);
            } else if (sourceAccountIdObj instanceof Number) {
                transaction.setSourceAccountId(((Number) sourceAccountIdObj).intValue());
            } else {
                transaction.setSourceAccountId(Integer.valueOf(sourceAccountIdObj.toString()));
            }
        }

        // Handle destination account ID (can be null)
        Object destinationAccountIdObj = rs.getObject("destination_account_id");
        if (destinationAccountIdObj != null) {
            if (destinationAccountIdObj instanceof Integer) {
                transaction.setDestinationAccountId((Integer) destinationAccountIdObj);
            } else if (destinationAccountIdObj instanceof Number) {
                transaction.setDestinationAccountId(((Number) destinationAccountIdObj).intValue());
            } else {
                transaction.setDestinationAccountId(Integer.valueOf(destinationAccountIdObj.toString()));
            }
        }

        transaction.setType(rs.getString("type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTimestamp(LocalDateTime.parse(rs.getString("timestamp")));
        transaction.setDescription(rs.getString("description"));
        return transaction;
    }
}