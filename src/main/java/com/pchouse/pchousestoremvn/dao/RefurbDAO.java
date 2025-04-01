package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Refurb;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RefurbDAO {

  private EntityManager entityManager = JPAUtil.getEntityManager();

    public List<Refurb> getAllRefurbProdDAO(Company pCompany) {
        try {
            TypedQuery<Refurb> query = entityManager.createQuery(
                    "FROM Refurb R WHERE R.company = :pCompany ORDER BY R.category ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public long addRefurbProductDAO(Refurb pRefurbProd) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(pRefurbProd);
            transaction.commit();
            return pRefurbProd.getIdRefurb();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    public Refurb getItemRefurbProdDAO(long pIdRefurbProd) {
        try {
            return entityManager.find(Refurb.class, pIdRefurbProd);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public boolean updateRefurbProdDAO(Refurb pRefurbProd) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(pRefurbProd);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRefurbProdDAO(Refurb pRefurbProd) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(pRefurbProd) ? pRefurbProd : entityManager.merge(pRefurbProd));
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Refurb> searchRefurbDAO(String pSearch) {
        try {
            String queryStr = "FROM Refurb R WHERE " +
                    "R.category LIKE :pSearch OR R.brand LIKE :pSearch OR R.model LIKE :pSearch OR R.price LIKE :pSearch " +
                    "OR R.qty LIKE :pSearch OR R.serialNumber LIKE :pSearch OR R.note LIKE :pSearch OR R.screen LIKE :pSearch " +
                    "OR R.processor LIKE :pSearch OR R.ramMemory LIKE :pSearch OR R.storage LIKE :pSearch OR R.gpuBoard LIKE :pSearch " +
                    "OR R.batteryHealth LIKE :pSearch OR R.custom1 LIKE :pSearch OR R.custom2 LIKE :pSearch OR R.custom3 LIKE :pSearch " +
                    "OR R.custom4 LIKE :pSearch OR R.custom5 LIKE :pSearch OR R.custom6 LIKE :pSearch";
            TypedQuery<Refurb> query = entityManager.createQuery(queryStr, Refurb.class);
            query.setParameter("pSearch", "%" + pSearch + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Refurb> getAllRefurbByCategoryDAO(Company pCompany, String pCategory) {
        try {
            TypedQuery<Refurb> query = entityManager.createQuery(
                    "FROM Refurb R WHERE R.company = :pCompany AND R.category = :category ORDER BY R.brand ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("category", pCategory);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Refurb> searchRefurbByCategoryDAO(Company pCompany, String pCategory, String pSearch) {
        try {
            String queryStr = "FROM Refurb R WHERE R.company = :pCompany AND R.category = :pCategory AND (" +
                    "R.brand LIKE :pSearch OR R.model LIKE :pSearch OR R.price LIKE :pSearch OR R.qty LIKE :pSearch " +
                    "OR R.serialNumber LIKE :pSearch OR R.note LIKE :pSearch OR R.screen LIKE :pSearch OR R.processor LIKE :pSearch " +
                    "OR R.ramMemory LIKE :pSearch OR R.storage LIKE :pSearch OR R.gpuBoard LIKE :pSearch " +
                    "OR R.batteryHealth LIKE :pSearch OR R.custom1 LIKE :pSearch OR R.custom2 LIKE :pSearch " +
                    "OR R.custom3 LIKE :pSearch OR R.custom4 LIKE :pSearch OR R.custom5 LIKE :pSearch OR R.custom6 LIKE :pSearch) " +
                    "ORDER BY R.brand ASC";
            TypedQuery<Refurb> query = entityManager.createQuery(queryStr, Refurb.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("pCategory", pCategory);
            query.setParameter("pSearch", "%" + pSearch + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Refurb> getAllCustomRefurbProdDAO(Company pCompany) {
        try {
            TypedQuery<Refurb> query = entityManager.createQuery(
                    "FROM Refurb R WHERE R.company = :pCompany " +
                    "AND R.category <> 'COMPUTER' AND R.category <> 'CONSOLE' AND R.category <> 'MONITOR' AND R.category <> 'TV' " +
                    "ORDER BY R.category ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Refurb> searchCustomRefurbDAO(Company pCompany, String pSearch) {
        try {
            String queryStr = "FROM Refurb R WHERE R.company = :company AND " +
                    "R.category <> 'COMPUTER' AND R.category <> 'CONSOLE' AND R.category <> 'MONITOR' AND R.category <> 'TV' " +
                    "AND (R.brand LIKE :pSearch OR R.model LIKE :pSearch OR R.price LIKE :pSearch OR R.qty LIKE :pSearch " +
                    "OR R.serialNumber LIKE :pSearch OR R.note LIKE :pSearch OR R.screen LIKE :pSearch OR R.processor LIKE :pSearch " +
                    "OR R.ramMemory LIKE :pSearch OR R.storage LIKE :pSearch OR R.gpuBoard LIKE :pSearch " +
                    "OR R.batteryHealth LIKE :pSearch OR R.custom1 LIKE :pSearch OR R.custom2 LIKE :pSearch " +
                    "OR R.custom3 LIKE :pSearch OR R.custom4 LIKE :pSearch OR R.custom5 LIKE :pSearch OR R.custom6 LIKE :pSearch) " +
                    "ORDER BY R.category ASC";
            TypedQuery<Refurb> query = entityManager.createQuery(queryStr, Refurb.class);
            query.setParameter("company", pCompany);
            query.setParameter("pSearch", "%" + pSearch + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
