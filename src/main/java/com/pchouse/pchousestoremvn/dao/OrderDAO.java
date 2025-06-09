package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.dao.PersonDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Device;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDAO {

    public long getLastOrderIdDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        long orderId = 0;
        try {
            TypedQuery<Long> query = em.createQuery("SELECT MAX(o.idOrder) FROM ServiceOrder o", Long.class);
            Long result = query.getSingleResult();
            orderId = (result != null) ? result : 0;
        } catch (Exception e) {
            System.err.println("Error retrieving last order ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderId;
    }

    public long addOrderDAO(ServiceOrder pOrderModel) throws BusinessException {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderAdded = 0;
        try {
            em.getTransaction().begin();

            Customer customer = pOrderModel.getCustomer();
            Person person = customer.getPerson();
            Device device = pOrderModel.getDevice();
            
            if (device != null && device.getIdDevice() == 0) {
                em.persist(device);
                em.flush();
            }

            if (person.getIdPerson() == 0) {
                em.persist(person);
                em.flush();
            }

            if (customer.getIdCustomer() == 0) {
                em.persist(customer);
                em.flush();
            } else {
                // If customer already exists, attach managed entity
                customer = em.find(Customer.class, customer.getIdCustomer());
            }

            pOrderModel.setCustomer(customer); // assign the managed or new customer

            // Certificar-se de que a Company está anexada ao contexto antes de persistir
            if (pOrderModel.getCompany() != null) {
                Company managedCompany = em.find(Company.class, pOrderModel.getCompany().getIdCompany());
                pOrderModel.setCompany(managedCompany);
            }

            // Persistir a ordem com suas associações gerenciadas
            ServiceOrder managedOrder = em.merge(pOrderModel);
            em.getTransaction().commit();
            idOrderAdded = managedOrder.getIdServiceOrder();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new BusinessException("Failed to add order: " + e.getMessage(), e);

        } finally {
            em.close();
        }
        return idOrderAdded;
    }

    public ServiceOrder getItemOrderDAO(long pIdOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        ServiceOrder itemOrder = null;
        try {
            itemOrder = em.find(ServiceOrder.class, pIdOrder);
        } catch (Exception e) {
            System.err.println("Error retrieving order by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return itemOrder;
    }

    public List<ServiceOrder> getAllOrderDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrder> listOrder = null;
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                    "FROM ServiceOrder o WHERE o.company = :pCompany ORDER BY o.created DESC", ServiceOrder.class);
            query.setParameter("pCompany", pCompany);
            listOrder = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrder;
    }

    public List<ServiceOrder> searchOrderDAO(Company pCompany, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrder> listOrder = null;
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                    "SELECT DISTINCT o FROM ServiceOrder o "
                    + "JOIN o.customer c JOIN c.person p "
                    + "LEFT JOIN o.device d "
                    + "WHERE o.company = :pCompany AND "
                    + "(p.firstName LIKE :pSearch OR p.lastName LIKE :pSearch OR p.contactNo LIKE :pSearch OR "
                    + "p.email LIKE :pSearch OR d.brand LIKE :pSearch OR d.model LIKE :pSearch OR "
                    + "d.serialNumber LIKE :pSearch OR CAST(o.idOrder AS string) LIKE :pSearch)", ServiceOrder.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listOrder = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrder;
    }

    public boolean updateOrderDAO(ServiceOrder pOrderModel) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;

        try {
            em.getTransaction().begin();

            // Reattach Company
            if (pOrderModel.getCompany() != null) {
                Company managedCompany = em.getReference(Company.class, pOrderModel.getCompany().getIdCompany());
                pOrderModel.setCompany(managedCompany);
            }

            // Reattach Customer, Customer's Company, and Person
            if (pOrderModel.getCustomer() != null) {
                Customer detachedCustomer = pOrderModel.getCustomer();

                if (detachedCustomer.getCompany() != null) {
                    Company managedCustomerCompany = em.getReference(Company.class, detachedCustomer.getCompany().getIdCompany());
                    detachedCustomer.setCompany(managedCustomerCompany);
                }

                if (detachedCustomer.getPerson() != null) {
                    Person managedPerson = em.getReference(Person.class, detachedCustomer.getPerson().getIdPerson());
                    detachedCustomer.setPerson(managedPerson);
                }

                Customer managedCustomer = em.getReference(Customer.class, detachedCustomer.getIdCustomer());
                pOrderModel.setCustomer(managedCustomer);
            }

            // Reattach Device
            if (pOrderModel.getDevice() != null) {
                Device managedDevice = em.getReference(Device.class, pOrderModel.getDevice().getIdDevice());
                pOrderModel.setDevice(managedDevice);
            }

            // Reattach Employee
            if (pOrderModel.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, pOrderModel.getEmployee().getIdEmployee());
                pOrderModel.setEmployee(managedEmployee);
            }

            // Merge and commit
            em.merge(pOrderModel);
            em.getTransaction().commit();
            success = true;

        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return success;
    }
}
