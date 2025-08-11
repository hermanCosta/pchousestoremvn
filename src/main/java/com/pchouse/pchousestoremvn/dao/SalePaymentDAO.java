package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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

    public List<SalePayment> getSalePaymentDAO(Sale pSale) {
        EntityManager em = JPAUtil.getEntityManager();
        List<SalePayment> payments = new ArrayList<>();

        try {
            em.getTransaction().begin();

            payments = em.createQuery(
                    "SELECT sp FROM SalePayment sp WHERE sp.sale.idSale = :idSale",
                    SalePayment.class
            )
                    .setParameter("idSale", pSale.getIdSale())
                    .getResultList();

            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error fetching sale payments: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return payments;
    }

}
