package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployeeDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Employee> listEmployee = null;
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
            listEmployee = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listEmployee;
    }

    public long addEmployeeDAO(Employee employee) {
        EntityManager em = JPAUtil.getEntityManager();
        long idEmployeeAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
            idEmployeeAdded = employee.getIdEmployee(); // Ajuste no nome do método
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idEmployeeAdded;
    }

    public boolean updateEmployeeDAO(Employee employee) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            em.merge(employee);
            em.getTransaction().commit();
            success = true;
        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return success;
    }

    public boolean deleteEmployeeDAO(long employeeId) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Employee employee = em.find(Employee.class, employeeId);
            if (employee != null) {
                em.remove(employee);
                success = true;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return success;
    }

    public Employee getEmployeeDAO(Person person) {
        EntityManager em = JPAUtil.getEntityManager();
        Employee employee = null;
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.person = :person", Employee.class);
            query.setParameter("person", person);
            employee = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error retrieving employee by person: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return employee;
    }

    public Employee getItemEmployeeDAO(long employeeId) {
        EntityManager em = JPAUtil.getEntityManager();
        Employee employee = null;
        try {
            employee = em.find(Employee.class, employeeId);
        } catch (Exception e) {
            System.err.println("Error retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return employee;
    }

    public Employee getEmployeeByPasswordDAO(String password) {
        EntityManager em = JPAUtil.getEntityManager();
        Employee employee = null;
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.password = :password", Employee.class);
            query.setParameter("password", password);
            employee = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error retrieving employee by password: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return employee;
    }

    public Employee searchEmployeeByContactNoDAO(String contactNo) {
        EntityManager em = JPAUtil.getEntityManager();
        Employee employee = null;
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.person.idPerson = " +
                "(SELECT p.idPerson FROM Person p WHERE p.contactNo = :contactNo)", Employee.class);
            query.setParameter("contactNo", contactNo);
            employee = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error searching employee by contact number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return employee;
    }
}
