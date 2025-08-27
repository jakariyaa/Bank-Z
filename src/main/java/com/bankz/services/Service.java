package com.bankz.services;

import java.util.List;

/**
 * Generic service interface with common operations
 * @param <T> The type of entity
 * @param <ID> The type of the entity's identifier
 */
public interface Service<T, ID> {
    
    /**
     * Saves an entity
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);
    
    /**
     * Finds an entity by its ID
     * @param id The ID of the entity to find
     * @return The entity if found, null otherwise
     */
    T findById(ID id);
    
    /**
     * Finds all entities
     * @return A list of all entities
     */
    List<T> findAll();
    
    /**
     * Updates an entity
     * @param entity The entity to update
     * @return true if the update was successful, false otherwise
     */
    boolean update(T entity);
    
    /**
     * Deletes an entity by its ID
     * @param id The ID of the entity to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete(ID id);
}