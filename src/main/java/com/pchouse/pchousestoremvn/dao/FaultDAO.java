package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Fault;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FaultDAO {

    public List<Fault> getAllFaultDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Fault> listFault = null;
        try {
            TypedQuery<Fault> query = em.createQuery("SELECT f FROM Fault f", Fault.class);
            listFault = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving faults: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listFault;
    }

    public long addFaultDAO(Fault fault) {
        EntityManager em = JPAUtil.getEntityManager();
        long idFaultAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(fault);
            em.getTransaction().commit();
            idFaultAdded = fault.getIdFault(); // Ajuste no nome do método
        } catch (Exception e) {
            System.err.println("Error adding fault: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idFaultAdded;
    }

    public boolean updateFaultDAO(Fault fault) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            em.merge(fault);
            em.getTransaction().commit();
            success = true;
        } catch (Exception e) {
            System.err.println("Error updating fault: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return success;
    }

    public boolean deleteFaultDAO(Fault fault) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Fault faultToRemove = em.find(Fault.class, fault.getIdFault());
            if (faultToRemove != null) {
                em.remove(faultToRemove);
                success = true;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting fault: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return success;
    }

    public List<Fault> searchFaultDAO(String search) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Fault> listFault = null;
        try {
            TypedQuery<Fault> query = em.createQuery(
                "SELECT f FROM Fault f WHERE f.description LIKE :search", Fault.class);
            query.setParameter("search", "%" + search + "%");
            listFault = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching faults: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listFault;
    }

    public Fault getItemFaultDAO(long idFault) {
        EntityManager em = JPAUtil.getEntityManager();
        Fault fault = null;
        try {
            fault = em.find(Fault.class, idFault);
        } catch (Exception e) {
            System.err.println("Error retrieving fault by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return fault;
    }

    public List<Fault> orderSearchFaultDAO(String search) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Fault> listFault = null;
        try {
            TypedQuery<Fault> query = em.createQuery(
                "SELECT f FROM Fault f WHERE f.description LIKE :search", Fault.class);
            query.setParameter("search", "%" + search + "%");
            query.setMaxResults(5);
            listFault = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching ordered faults: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listFault;
    }

    public long checkExistFaultDAO(String search) {
        EntityManager em = JPAUtil.getEntityManager();
        long idExistFault = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT f.idFault FROM Fault f WHERE f.description = :search", Long.class);
            query.setParameter("search", search);
            idExistFault = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error checking if fault exists: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return idExistFault;
    }
}
