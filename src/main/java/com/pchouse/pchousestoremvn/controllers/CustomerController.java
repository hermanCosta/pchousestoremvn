package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CustomerDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Person;
import java.util.List;

public class CustomerController {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getAllCustomers(Company company) {
        return customerDAO.getAllCustomerDAO(company);
    }

    public long addCustomer(Customer customer) throws BusinessException {
        return customerDAO.addCustomerDAO(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomerDAO(customer);
    }

    public boolean deleteCustomer(long customerId) {
        return customerDAO.deleteCustomerDAO(customerId);
    }

    public Customer getCustomer(Person person) {
        return customerDAO.getCustomerDAO(person);
    }
    
    public Customer getCustomerById(long customerId) {
        return customerDAO.getItemCustomerDAO(customerId);
    }
    
    public Customer searchCustomerByContactNo(String contactNo) {
        return customerDAO.searchCustomerByContactNoDAO(contactNo);
    }
}
