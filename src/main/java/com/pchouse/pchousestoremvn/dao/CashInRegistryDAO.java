package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.CashInRegistry;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;

import java.util.List;

public class CashInRegistryDAO {

    /**
     * Retrieves the most recent 14 CashInRegistry records for a given company.
     */
    public List<CashInRegistry> getAllCashInByEmployee(Company company) {
        EntityManager em = JPAUtil.getEntityManager();
        List<CashInRegistry> cashInList = null;

        try {
            TypedQuery<CashInRegistry> query = em.createQuery(
                    "SELECT c FROM CashInRegistry c WHERE c.company = :company ORDER BY c.transactionDate DESC",
                    CashInRegistry.class
            );
            query.setParameter("company", company);
            cashInList = query.setMaxResults(14).getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving cash-in list: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return cashInList;
    }

    /**
     * Persists a new CashInRegistry entry.
     */
    public boolean insertCashIn(CashInRegistry cashInRegistry) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cashInRegistry);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting CashInRegistry: " + e.getMessage());
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public List<CashInRegistry> getAllCashInByDateRange(Company currentCompany, Date from, Date to) {
        EntityManager em = JPAUtil.getEntityManager(); 
        try {
            TypedQuery<CashInRegistry> query = em.createQuery(
                    "SELECT c FROM CashInRegistry c WHERE c.company = :company AND c.date BETWEEN :from AND :to ORDER BY c.date ASC",
                    CashInRegistry.class
            );
            query.setParameter("company", currentCompany);
            query.setParameter("from", from);
            query.setParameter("to", to);

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
