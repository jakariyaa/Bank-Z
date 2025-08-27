package com.bankz.services;

import java.sql.SQLException;

import com.bankz.dao.CustomerDao;
import com.bankz.dao.EmployeeDao;
import com.bankz.models.Customer;
import com.bankz.models.Employee;
import com.bankz.util.PasswordUtils;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerDao customerDao;
    private final EmployeeDao employeeDao;

    public AuthenticationServiceImpl(CustomerDao customerDao, EmployeeDao employeeDao) {
        this.customerDao = customerDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public Customer authenticateCustomer(String username, String password) {
        try {
            Customer customer = customerDao.findByUsername(username);
            if (customer != null && PasswordUtils.verifyPassword(password, customer.getPasswordHash())) {
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee authenticateEmployee(String username, String password) {
        try {
            Employee employee = employeeDao.findByUsername(username);
            if (employee != null && PasswordUtils.verifyPassword(password, employee.getPasswordHash())) {
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer registerCustomer(String firstName, String lastName, String username, String password) {
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
    public Employee registerEmployee(String firstName, String lastName, String username, String password, String role) {
        try {
            if (employeeDao.usernameExists(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
            
            String hashedPassword = PasswordUtils.hashPassword(password);
            
            Employee employee = new Employee(firstName, lastName, username, hashedPassword, role);
            return employeeDao.save(employee);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}