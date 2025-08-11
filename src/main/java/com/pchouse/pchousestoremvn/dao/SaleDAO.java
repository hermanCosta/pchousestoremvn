package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SaleDAO {

    public long getLastSaleIdDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        long saleId = 0;
        try {
            TypedQuery<Long> query = em.createQuery("SELECT MAX(s.idSale) FROM Sale s", Long.class);
            Long result = query.getSingleResult();
            saleId = (result != null) ? result : 0;
        } catch (Exception e) {
            System.err.println("Error retrieving last sale ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return saleId;
    }

    public long addSaleDAO(Sale pSaleModel) throws BusinessException {
        EntityManager em = JPAUtil.getEntityManager();
        long idSaleAdded = 0;
        try {
            em.getTransaction().begin();

            Customer customer = pSaleModel.getCustomer();
            Person person = customer.getPerson();

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

            pSaleModel.setCustomer(customer); // assign the managed or new customer

            // Certificar-se de que a Company está anexada ao contexto antes de persistir
            if (pSaleModel.getCompany() != null) {
                Company managedCompany = em.find(Company.class, pSaleModel.getCompany().getIdCompany());
                pSaleModel.setCompany(managedCompany);
            }

            // Persistir a ordem com suas associações gerenciadas
            Sale managedSale = em.merge(pSaleModel);
            em.getTransaction().commit();
            idSaleAdded = managedSale.getIdSale();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new BusinessException("Failed to add sale: " + e.getMessage(), e);

        } finally {
            em.close();
        }
        return idSaleAdded;
    }

    public Sale getItemSaleDAO(long pIdSale) {
        EntityManager em = JPAUtil.getEntityManager();
        Sale itemSale = null;
        try {
            itemSale = em.find(Sale.class, pIdSale);
        } catch (Exception e) {
            System.err.println("Error retrieving sale by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return itemSale;
    }

    public List<Sale> getAllSaleDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Sale> listSale = null;
        try {
            TypedQuery<Sale> query = em.createQuery(
                    "FROM Sale s WHERE s.company = :pCompany ORDER BY s.created DESC", Sale.class);
            query.setParameter("pCompany", pCompany);
            listSale = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all sales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listSale;
    }

    public List<Sale> searchSaleDAO(Company pCompany, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Sale> listSale = null;
        try {
            TypedQuery<Sale> query = em.createQuery(
                    "SELECT DISTINCT s FROM Sale s "
                    + "JOIN s.customer c JOIN c.person p "
                    + "WHERE s.company = :pCompany AND "
                    + "(p.firstName LIKE :pSearch OR p.lastName LIKE :pSearch OR "
                    + "p.contactNo LIKE :pSearch OR p.email LIKE :pSearch OR "
                    + "CAST(s.idSale AS string) LIKE :pSearch)", Sale.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listSale = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching sales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listSale;
    }

    public boolean updateSaleDAO(Sale pSaleModel) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;

        try {
            em.getTransaction().begin();

            // Reattach Company
            if (pSaleModel.getCompany() != null) {
                Company managedCompany = em.getReference(Company.class, pSaleModel.getCompany().getIdCompany());
                pSaleModel.setCompany(managedCompany);
            }

            // Reattach Customer, Customer's Company, and Person
            if (pSaleModel.getCustomer() != null) {
                Customer detachedCustomer = pSaleModel.getCustomer();

                if (detachedCustomer.getCompany() != null) {
                    Company managedCustomerCompany = em.getReference(Company.class, detachedCustomer.getCompany().getIdCompany());
                    detachedCustomer.setCompany(managedCustomerCompany);
                }

                if (detachedCustomer.getPerson() != null) {
                    Person managedPerson = em.getReference(Person.class, detachedCustomer.getPerson().getIdPerson());
                    detachedCustomer.setPerson(managedPerson);
                }

                Customer managedCustomer = em.getReference(Customer.class, detachedCustomer.getIdCustomer());
                pSaleModel.setCustomer(managedCustomer);
            }

            // Reattach Employee
            if (pSaleModel.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, pSaleModel.getEmployee().getIdEmployee());
                pSaleModel.setEmployee(managedEmployee);
            }

            // Merge and commit
            em.merge(pSaleModel);
            em.getTransaction().commit();
            success = true;

        } catch (Exception e) {
            System.err.println("Error updating sale: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return success;
    }

    public long addOrderSaleDAO(
            Sale sale,
            List<SaleProdServ> items,
            List<SalePayment> payments,
            Deposit deposit,
            OrderNote note) throws Exception {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Persist Person if new
            Customer customer = sale.getCustomer();
            Person person = customer.getPerson();

            if (person.getIdPerson() == 0) {
                em.persist(person);
                em.flush(); // ensure ID generated
            }

            // Persist Customer if new, else reattach managed instance
            if (customer.getIdCustomer() == 0) {
                em.persist(customer);
                em.flush();
            } else {
                customer = em.find(Customer.class, customer.getIdCustomer());
            }
            sale.setCustomer(customer);

            // Reattach Employee to managed entity (for Sale)
            if (sale.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, sale.getEmployee().getIdEmployee());
                sale.setEmployee(managedEmployee);
            }

            // Reattach Company to managed entity (if present)
            if (sale.getCompany() != null) {
                Company managedCompany = em.find(Company.class, sale.getCompany().getIdCompany());
                sale.setCompany(managedCompany);
            }

            // Persist Sale
            em.persist(sale);

            // Persist SaleProdServ items
            if (items != null) {
                for (SaleProdServ item : items) {
                    item.setSale(sale);
                    em.persist(item);
                }
            }

            // Persist Deposit
            if (deposit != null) {
                deposit.setSale(sale);
                //deposit.setSalePayment(payment);

                // Reattach Employee for Deposit
                if (deposit.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, deposit.getEmployee().getIdEmployee());
                    deposit.setEmployee(managedEmployee);
                }

                em.persist(deposit);
            }

                        // Persist SalePayment
            if (payments != null) {

                for (SalePayment payment : payments) {
                    payment.setDeposit(deposit);
                    payment.setSale(sale);
                    em.persist(payment);
                }
            }
            
            // Persist OrderNote
            if (note != null) {
                note.setSale(sale);

                // Reattach Employee for Note
                if (note.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, note.getEmployee().getIdEmployee());
                    note.setEmployee(managedEmployee);
                }

                em.persist(note);
            }

            tx.commit();

            return sale.getIdSale();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new BusinessException("Failed to add sale: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
