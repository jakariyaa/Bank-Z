package com.bankz.services;

import com.bankz.dao.CustomerDao;
import com.bankz.models.Customer;
import com.bankz.util.PasswordUtils;

import java.sql.SQLException;
import java.util.List;


public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerDao customerDao;
    
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }
    
    @Override
    public Customer createCustomer(String firstName, String lastName, String username, String password) {
        try {
            if (customerDao.usernameExists(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
            
            String hashedPassword = PasswordUtils.hashPassword(password);
            
            Customer customer = new Customer(firstName, lastName, username, hashedPassword);
            return customerDao.save(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Customer findCustomerById(int customerId) {
        try {
            return customerDao.findById(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Customer findCustomerByUsername(String username) {
        try {
            return customerDao.findByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        try {
            return customerDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean updateCustomer(Customer customer) {
        try {
            return customerDao.update(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteCustomer(int customerId) {
        try {
            return customerDao.delete(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}