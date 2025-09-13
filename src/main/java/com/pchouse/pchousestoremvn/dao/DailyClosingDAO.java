package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.DailyClosing;
import com.pchouse.pchousestoremvn.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Date;
import java.util.List;

public class DailyClosingDAO {

    // Add a new daily closing
    public int addDailyClosingDAO(DailyClosing dailyClosing) {
        EntityManager em = JPAUtil.getEntityManager();
        int idAdded = 0;

        try {
            em.getTransaction().begin();

            // Reattach employee
            if (dailyClosing.getEmployee() != null) {
                dailyClosing.setEmployee(
                        em.find(dailyClosing.getEmployee().getClass(), dailyClosing.getEmployee().getIdEmployee())
                );
            }

            em.persist(dailyClosing);
            em.getTransaction().commit();

            idAdded = dailyClosing.getIdDailyClosing();

        } catch (Exception e) {
            System.err.println("Error adding daily closing: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return idAdded;
    }

    // Check if date is already closed
    public boolean isDateAlreadyClosedDAO(Date date) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean exists = false;

        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM DailyClosing c WHERE c.closingDate = :date", Long.class
            );
            query.setParameter("date", date);
            Long count = query.getSingleResult();
            exists = count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Error checking if date is already closed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return exists;
    }

    // Get closing by date
    public DailyClosing getClosingByDateDAO(Date date) {
        EntityManager em = JPAUtil.getEntityManager();
        DailyClosing closing = null;

        try {
            TypedQuery<DailyClosing> query = em.createQuery(
                    "SELECT c FROM DailyClosing c WHERE c.closingDate = :date", DailyClosing.class
            );
            query.setParameter("date", date);
            closing = query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            System.err.println("Error fetching closing by date: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return closing;
    }

    // Optional: Get all closings between two dates
    public List<DailyClosing> getAllClosingsDAO(Date from, Date to) {
        EntityManager em = JPAUtil.getEntityManager();
        List<DailyClosing> closings = null;

        try {
            TypedQuery<DailyClosing> query = em.createQuery(
                    "SELECT c FROM DailyClosing c WHERE c.closingDate BETWEEN :from AND :to ORDER BY c.closingDate DESC",
                    DailyClosing.class
            );
            query.setParameter("from", from);
            query.setParameter("to", to);
            closings = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching closings in range: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return closings;
    }
}
