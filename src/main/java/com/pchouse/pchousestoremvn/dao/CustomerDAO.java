package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomerDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Customer> customers = null;
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c ORDER BY c.person ASC", Customer.class);
            customers = query.setMaxResults(14).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return customers;
    }

    public long addCustomerDAO(Customer customer) throws BusinessException {
        EntityManager em = JPAUtil.getEntityManager();
        long idCustomer = 0;
        try {
            em.getTransaction().begin();

            Person person = customer.getPerson();

            if (person.getIdPerson() == 0) {
                em.persist(person);
                em.flush();
            }

            Customer managedCustomer = em.merge(customer);
            em.getTransaction().commit();
            idCustomer = managedCustomer.getIdCustomer();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new BusinessException("Failed to add customer: " + e.getMessage(), e);

        } finally {
            em.close();
        }
        return idCustomer;
    }

    public boolean updateCustomerDAO(Customer customer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(customer.getPerson());
            em.merge(customer);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean deleteCustomerDAO(long customerId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, customerId);
            if (customer != null) {
                em.remove(customer);
                em.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public Customer getCustomerDAO(Person person) {
        EntityManager em = JPAUtil.getEntityManager();
        Customer customer = null;
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.person = :person", Customer.class);
            query.setParameter("person", person);
            customer = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return customer;
    }

    public Customer getItemCustomerDAO(long customerId) {
        EntityManager em = JPAUtil.getEntityManager();
        Customer customer = null;
        try {
            customer = em.find(Customer.class, customerId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return customer;
    }

    public Customer searchCustomerByContactNoDAO(String contactNo) {
        EntityManager em = JPAUtil.getEntityManager();
        Customer customer = null;
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.person.id = (SELECT p.id FROM Person p WHERE p.contactNo = :contactNo)",
                    Customer.class);
            query.setParameter("contactNo", contactNo);
            customer = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return customer;
    }
}
