package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
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

    public long addRefurbSalePaymentDAO(List<SalePayment> pSalePayments, OrderNote note) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        SalePayment lastPayment = null;

        try {
            em.getTransaction().begin();

            if (pSalePayments != null && !pSalePayments.isEmpty()) {
                for (SalePayment payment : pSalePayments) {
                    if (payment.getSale() != null) {
                        // Reattach ServiceOrder properly
                        Sale managedSale = em.getReference(
                                Sale.class,
                                payment.getSale().getIdSale()
                        );
                        payment.setSale(managedSale);
                    }

                    em.persist(payment);
                    lastPayment = payment;
                }

                // Update status AFTER persisting last payment
                if (lastPayment != null && lastPayment.getSale() != null) {
                    lastPayment.getSale().setStatus(OrderStatus.PICKED);
                }
            }

            // Persist OrderNote
            if (note != null && lastPayment != null) {
                note.setSale(lastPayment.getSale());

                // Reattach Employee
                if (note.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(
                            Employee.class,
                            note.getEmployee().getIdEmployee()
                    );
                    note.setEmployee(managedEmployee);
                }

                em.persist(note);
            }

            em.getTransaction().commit();
            return lastPayment != null ? lastPayment.getIdSalePayment() : -1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new BusinessException("Failed to add payment: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<SalePayment> getAllByDate(Date date) {
        EntityManager em = JPAUtil.getEntityManager();
        List<SalePayment> list = null;
        try {
            TypedQuery<SalePayment> query = em.createQuery(
                    "SELECT s FROM SalePayment s WHERE DATE(s.dtTransaction) = DATE(:date)",
                    SalePayment.class
            );
            query.setParameter("date", date);
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return list;
    }

}
