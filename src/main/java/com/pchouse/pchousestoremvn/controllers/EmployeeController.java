package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.EmployeeDAO;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import java.util.List;

public class EmployeeController {

    private final EmployeeDAO EMPLOYEE_DAO = new EmployeeDAO();

    // Fetches all employees from the database
    public List<Employee> getAllEmployee() {
        return EMPLOYEE_DAO.getAllEmployeeDAO();
    }

    // Adds a new employee to the database
    public long addEmployee(Employee pEmployee) {
        return EMPLOYEE_DAO.addEmployeeDAO(pEmployee);
    }

    // Updates the information of an existing employee
    public boolean updateEmployee(Employee pEmployee) {
        return EMPLOYEE_DAO.updateEmployeeDAO(pEmployee);
    }

    // Deletes an employee by their ID
    public boolean deleteEmployee(long pId) {
        return EMPLOYEE_DAO.deleteEmployeeDAO(pId);
    }

    // Fetches an employee based on the associated person details
    public Employee getEmployee(Person pPerson) {
        return EMPLOYEE_DAO.getEmployeeDAO(pPerson);
    }

    // Retrieves an employee's details using their employee ID
    public Employee getItemEmployee(long pIdEmployee) {
        return EMPLOYEE_DAO.getItemEmployeeDAO(pIdEmployee);
    }

    // Fetches an employee by their password string
    public Employee getEmployeeByPass(String pPassStr) {
        return EMPLOYEE_DAO.getEmployeeByPasswordDAO(pPassStr);
    }

    // Searches for an employee by their contact number
    public Employee searchEmployeeByContactNo(String pContactNo) {
        return EMPLOYEE_DAO.searchEmployeeByContactNoDAO(pContactNo);
    }
}
