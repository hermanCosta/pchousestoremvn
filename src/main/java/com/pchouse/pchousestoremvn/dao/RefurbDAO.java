package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Refurb;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RefurbDAO {

    public List<Refurb> getAllRefurbProdDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> result = null;
        try {
            TypedQuery<Refurb> query = em.createQuery(
                    "FROM Refurb r WHERE r.company = :pCompany ORDER BY r.category ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            result = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    public List<Refurb> getAllCustomRefurbProdDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> listRefurbs = null;
        try {
            TypedQuery<Refurb> query = em.createQuery(
                    "FROM Refurb r WHERE r.company = :pCompany "
                    + "AND r.category <> 'COMPUTER' AND r.category <> 'CONSOLE' "
                    + "AND r.category <> 'MONITOR' AND r.category <> 'TV' "
                    + "ORDER BY r.category ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            listRefurbs = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting custom refurb products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbs;
    }

    public long addRefurbProductDAO(Refurb pRefurbProd) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Refurb managedRefurb = em.merge(pRefurbProd);
            transaction.commit();
            return managedRefurb.getIdRefurb();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error adding refurb: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            em.close();
        }
    }

    public Refurb getItemRefurbProdDAO(long pIdRefurbProd) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Refurb.class, pIdRefurbProd);
        } catch (Exception e) {
            System.err.println("Error fetching refurb item: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public boolean updateRefurbProdDAO(Refurb pRefurbProd) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(pRefurbProd);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating refurb: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deleteRefurbProdDAO(Refurb pRefurbProd) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Refurb managed = em.contains(pRefurbProd) ? pRefurbProd : em.merge(pRefurbProd);
            em.remove(managed);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error deleting refurb: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public List<Refurb> searchRefurbDAO(String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> listRefurbs = null;
        try {
            TypedQuery<Refurb> query = em.createQuery("SELECT DISTINCT r FROM Refurb r WHERE "
                    + "r.category LIKE :pSearch OR r.brand LIKE :pSearch OR r.model LIKE :pSearch OR "
                    + "CAST(r.price AS string) LIKE :pSearch OR CAST(r.qty AS string) LIKE :pSearch OR "
                    + "r.serialNumber LIKE :pSearch OR r.note LIKE :pSearch OR r.screen LIKE :pSearch OR "
                    + "r.processor LIKE :pSearch OR r.ramMemory LIKE :pSearch OR r.storage LIKE :pSearch OR "
                    + "r.gpuBoard LIKE :pSearch OR CAST(r.batteryHealth AS string) LIKE :pSearch OR "
                    + "r.custom1 LIKE :pSearch OR r.custom2 LIKE :pSearch OR r.custom3 LIKE :pSearch OR "
                    + "r.custom4 LIKE :pSearch OR r.custom5 LIKE :pSearch OR r.custom6 LIKE :pSearch", Refurb.class);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listRefurbs = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching refurb: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbs;
    }

    public List<Refurb> getAllRefurbByCategoryDAO(Company pCompany, String pCategory) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> listRefurbs = null;
        try {
            TypedQuery<Refurb> query = em.createQuery(
                    "FROM Refurb r WHERE r.company = :pCompany AND r.category = :category ORDER BY r.brand ASC", Refurb.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("category", pCategory);
            listRefurbs = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting refurb by category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbs;
    }

    public List<Refurb> searchRefurbByCategoryDAO(Company pCompany, String pCategory, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> listRefurbs = null;
        try {
            String queryStr = "FROM Refurb r WHERE r.company = :pCompany AND r.category = :pCategory AND ("
                    + "r.brand LIKE :pSearch OR r.model LIKE :pSearch OR CAST(r.price AS string) LIKE :pSearch OR "
                    + "CAST(r.qty AS string) LIKE :pSearch OR r.serialNumber LIKE :pSearch OR r.note LIKE :pSearch OR "
                    + "r.screen LIKE :pSearch OR r.processor LIKE :pSearch OR r.ramMemory LIKE :pSearch OR "
                    + "r.storage LIKE :pSearch OR r.gpuBoard LIKE :pSearch OR CAST(r.batteryHealth AS string) LIKE :pSearch OR "
                    + "r.custom1 LIKE :pSearch OR r.custom2 LIKE :pSearch OR r.custom3 LIKE :pSearch OR r.custom4 LIKE :pSearch OR "
                    + "r.custom5 LIKE :pSearch OR r.custom6 LIKE :pSearch) ORDER BY r.brand ASC";
            TypedQuery<Refurb> query = em.createQuery(queryStr, Refurb.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("pCategory", pCategory);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listRefurbs = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching refurb by category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbs;
    }

    public List<Refurb> searchCustomRefurbDAO(Company pCompany, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refurb> listRefurbs = null;
        try {
            String queryStr = "FROM Refurb r WHERE r.company = :company AND "
                    + "r.category <> 'COMPUTER' AND r.category <> 'CONSOLE' AND r.category <> 'MONITOR' AND r.category <> 'TV' AND ("
                    + "r.brand LIKE :pSearch OR r.model LIKE :pSearch OR CAST(r.price AS string) LIKE :pSearch OR "
                    + "CAST(r.qty AS string) LIKE :pSearch OR r.serialNumber LIKE :pSearch OR r.note LIKE :pSearch OR "
                    + "r.screen LIKE :pSearch OR r.processor LIKE :pSearch OR r.ramMemory LIKE :pSearch OR "
                    + "r.storage LIKE :pSearch OR r.gpuBoard LIKE :pSearch OR CAST(r.batteryHealth AS string) LIKE :pSearch OR "
                    + "r.custom1 LIKE :pSearch OR r.custom2 LIKE :pSearch OR r.custom3 LIKE :pSearch OR r.custom4 LIKE :pSearch OR "
                    + "r.custom5 LIKE :pSearch OR r.custom6 LIKE :pSearch) ORDER BY r.category ASC";
            TypedQuery<Refurb> query = em.createQuery(queryStr, Refurb.class);
            query.setParameter("company", pCompany);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listRefurbs = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching custom refurb: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbs;
    }
}
