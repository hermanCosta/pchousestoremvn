package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class OrderPaymentDAO {

    public long addOrderPaymentDAO(ServiceOrderPayment pOrderPayment) {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderPaymentAdded = 0;
        try {
            em.getTransaction().begin();

            // Reattach ServiceOrder
            if (pOrderPayment.getServiceOrder() != null) {
                ServiceOrder managedOrder = em.find(
                        ServiceOrder.class,
                        pOrderPayment.getServiceOrder().getIdServiceOrder()
                );
                pOrderPayment.setServiceOrder(managedOrder);
            }

            em.persist(pOrderPayment);
            em.getTransaction().commit();
            idOrderPaymentAdded = pOrderPayment.getIdOrderPayment();
        } catch (Exception e) {
            System.err.println("Error adding order payment: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idOrderPaymentAdded;
    }
}
