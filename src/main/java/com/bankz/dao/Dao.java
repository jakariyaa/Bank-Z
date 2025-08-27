package com.bankz.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO interface with common CRUD operations
 * @param <T> The type of entity
 * @param <ID> The type of the entity's identifier
 */
public interface Dao<T, ID> {
    
    /**
     * Saves an entity to the database
     * @param entity The entity to save
     * @return The saved entity with generated ID
     * @throws SQLException if a database access error occurs
     */
    T save(T entity) throws SQLException;
    
    /**
     * Finds an entity by its ID
     * @param id The ID of the entity to find
     * @return The entity if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    T findById(ID id) throws SQLException;
    
    /**
     * Finds all entities
     * @return A list of all entities
     * @throws SQLException if a database access error occurs
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Updates an entity in the database
     * @param entity The entity to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean update(T entity) throws SQLException;
    
    /**
     * Deletes an entity by its ID
     * @param id The ID of the entity to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    boolean delete(ID id) throws SQLException;
}