package com.orders_management.logic;

import com.orders_management.models.Customer;

import java.util.List;
import java.util.NoSuchElementException;
import com.orders_management.data_access.CustomerDAO;
public class CustomerBLL {

    public static List<String> getCustomerAttributes()
    {
        CustomerDAO customerDAO=new CustomerDAO();
        return customerDAO.getAttributeNamesWithoutId();
    }
    public static Customer findCustomerById(int id) {
        CustomerDAO customerDAO=new CustomerDAO();
        Customer customer = (Customer) customerDAO.findById(id);
        if (customer == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }
        return customer;
    }

    public static List<Customer> findAllCustomers()
    {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.findAll();
        return customers;
    }
    public static Customer insertCustomer(Customer customer) {
        if(!Validator.isAge(customer.getAge()))
        {
            throw new IllegalArgumentException("Age is not between the range 18-99.");
        }
        if(!Validator.containsOnlyLettersAndSpaces(customer.getName()))
        {
            throw new IllegalArgumentException("Name Field is invalid.");
        }
        if(!Validator.isValidEmail(customer.getEmail()))
        {
            throw new IllegalArgumentException("Email Field is invalid.");
        }
        CustomerDAO customerDAO=new CustomerDAO();
        Customer new_customer = customerDAO.insert(customer);
        return new_customer;
    }

    public static Customer updateCustomer(Customer customer) {
        if(!Validator.isAge(customer.getAge()))
        {
            throw new IllegalArgumentException("Age is not between the range 18-99.");
        }
        if(!Validator.containsOnlyLettersAndSpaces(customer.getName()))
        {
            throw new IllegalArgumentException("Name Field is invalid.");
        }
        if(!Validator.isValidEmail(customer.getEmail()))
        {
            throw new IllegalArgumentException("Email Field is invalid.");
        }
        CustomerDAO customerDAO=new CustomerDAO();
        Customer new_customer = customerDAO.update(customer);
        return new_customer;
    }

    public static boolean deleteCustomer(Customer customer) {
        CustomerDAO customerDAO=new CustomerDAO();
        boolean flag = customerDAO.delete(customer);
        return flag;
    }






}
