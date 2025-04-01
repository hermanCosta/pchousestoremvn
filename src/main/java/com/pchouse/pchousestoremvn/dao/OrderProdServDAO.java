package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderProdServDAO {

    public long addOrderProdServDAO(ServiceOrderProdServ pOrderProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderProdServAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(pOrderProdServ);
            em.getTransaction().commit();
            idOrderProdServAdded = pOrderProdServ.getIdServiceOrderProdServ();
        } catch (Exception e) {
            System.err.println("Error adding OrderProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idOrderProdServAdded;
    }

    public List<ServiceOrderProdServ> getOrderProdServDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrderProdServ> listOrderProdServ = null;
        try {
            em.getTransaction().begin();
            listOrderProdServ = em.createQuery(
                    "SELECT o FROM ServiceOrderProdServ o WHERE o.order = :pOrder", ServiceOrderProdServ.class)
                    .setParameter("pOrder", pOrder)
                    .getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error retrieving OrderProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return listOrderProdServ;
    }

    public long deleteOrderProdServDAO(long pIdOrderProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long result = 0;
        try {
            em.getTransaction().begin();
            result = em.createQuery("DELETE FROM ServiceOrderProdServ o WHERE o.idOrderProdServ = :pIdOrderProdServ")
                    .setParameter("pIdOrderProdServ", pIdOrderProdServ)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting OrderProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return result;
    }
}
