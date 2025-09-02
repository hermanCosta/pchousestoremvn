package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.CashOutRegistry;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CashOutRegistryDAO {

    /**
     * Retrieves the most recent 14 CashOutRegistry records for a given company.
     */
    public List<CashOutRegistry> getAllCashOutByEmployee(Company company) {
        EntityManager em = JPAUtil.getEntityManager();
        List<CashOutRegistry> cashOutList = null;

        try {
            TypedQuery<CashOutRegistry> query = em.createQuery(
                    "SELECT c FROM CashOutRegistry c WHERE c.company = :company ORDER BY c.transactionDate DESC",
                    CashOutRegistry.class
            );
            query.setParameter("company", company);
            cashOutList = query.setMaxResults(14).getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving cash-out list: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return cashOutList;
    }

    /**
     * Persists a new CashOutRegistry entry.
     */
    public boolean insertCashOut(CashOutRegistry cashOutRegistry) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cashOutRegistry);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting CashOutRegistry: " + e.getMessage());
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
}
