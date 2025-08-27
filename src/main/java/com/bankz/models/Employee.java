package com.bankz.models;

import java.time.LocalDateTime;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String role;
    private LocalDateTime dateHired;
    
    public Employee() {}
    
    public Employee(String firstName, String lastName, String username, String passwordHash, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.dateHired = LocalDateTime.now();
    }
    
    public Employee(int employeeId, String firstName, String lastName, String username, 
                   String passwordHash, String role, LocalDateTime dateHired) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.dateHired = dateHired;
    }
    
    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public LocalDateTime getDateHired() {
        return dateHired;
    }
    
    public void setDateHired(LocalDateTime dateHired) {
        this.dateHired = dateHired;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", dateHired=" + dateHired +
                '}';
    }
}