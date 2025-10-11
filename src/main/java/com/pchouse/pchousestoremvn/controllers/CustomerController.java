package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CustomerDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Person;
import java.util.List;

public class CustomerController {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getAllCustomers() throws BusinessException {
        try {
            return customerDAO.getAllCustomerDAO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error retrieving customer list: " + e.getMessage());
        }
    }

    public long addCustomer(Customer customer) throws BusinessException {
        try {
            return customerDAO.addCustomerDAO(customer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error adding customer: " + e.getMessage());
        }
    }

    public boolean updateCustomer(Customer customer) throws BusinessException {
        try {
            return customerDAO.updateCustomerDAO(customer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error updating customer: " + e.getMessage());
        }
    }

    public boolean deleteCustomer(long customerId) throws BusinessException {
        try {
            return customerDAO.deleteCustomerDAO(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error deleting customer: " + e.getMessage());
        }
    }

    public Customer getCustomer(Person person) throws BusinessException {
        try {
            return customerDAO.getCustomerDAO(person);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error fetching customer by person: " + e.getMessage());
        }
    }

    public Customer getCustomerById(long customerId) throws BusinessException {
        try {
            return customerDAO.getItemCustomerDAO(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error fetching customer by ID: " + e.getMessage());
        }
    }

    public Customer searchCustomerByContactNo(String contactNo) throws BusinessException {
        try {
            return customerDAO.searchCustomerByContactNoDAO(contactNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error searching customer by contact number: " + e.getMessage());
        }
    }
}
