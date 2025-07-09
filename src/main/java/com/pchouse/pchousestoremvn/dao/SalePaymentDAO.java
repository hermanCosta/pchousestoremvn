package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class SalePaymentDAO {

    public long addSalePaymentDAO(SalePayment pSalePayment) {
        EntityManager em = JPAUtil.getEntityManager();
        long idSalePaymentAdded = 0;
        try {
            em.getTransaction().begin();

            // Reattach Sale
            if (pSalePayment.getSale() != null) {
                Sale managedSale = em.find(
                        Sale.class,
                        pSalePayment.getSale().getIdSale()
                );
                pSalePayment.setSale(managedSale);
            }

            em.persist(pSalePayment);
            em.getTransaction().commit();
            idSalePaymentAdded = pSalePayment.getIdSalePayment();
        } catch (Exception e) {
            System.err.println("Error adding sale payment: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idSalePaymentAdded;
    }
}
