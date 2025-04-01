package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDAO {

    public long getLastOrderIdDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        long orderId = 0;
        try {
            TypedQuery<Long> query = em.createQuery("SELECT MAX(o.idOrder) FROM ServiceOrder o", Long.class);
            Long result = query.getSingleResult();
            orderId = (result != null) ? result : 1;
        } catch (Exception e) {
            System.err.println("Error retrieving last order ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderId;
    }

    public long addOrderDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(pOrder);
            em.getTransaction().commit();
            idOrderAdded = pOrder.getIdServiceOrder(); // Corrigido para usar o método correto
        } catch (Exception e) {
            System.err.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
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
                "SELECT DISTINCT o FROM ServiceOrder o " +
                "JOIN o.customer c JOIN c.person p " +
                "LEFT JOIN o.device d " +
                "WHERE o.company = :pCompany AND " +
                "(p.firstName LIKE :pSearch OR p.lastName LIKE :pSearch OR p.contactNo LIKE :pSearch OR " +
                "p.email LIKE :pSearch OR d.brand LIKE :pSearch OR d.model LIKE :pSearch OR " +
                "d.serialNumber LIKE :pSearch OR CAST(o.idOrder AS string) LIKE :pSearch)", ServiceOrder.class);
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
