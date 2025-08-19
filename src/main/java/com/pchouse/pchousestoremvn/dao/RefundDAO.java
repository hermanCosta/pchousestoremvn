/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Refund;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RefundDAO {

    private EntityManager em = JPAUtil.getEntityManager();

    public List<Refund> getAllRefundDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Refund> refunds = null;
        try {
            TypedQuery<Refund> query = em.createQuery(
                    "SELECT FROM Refund s WHERE s.company = :pCompany ORDER BY s.created DESC", Refund.class);
            query.setParameter("pCompany", pCompany);
            refunds = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all Refunds: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return refunds;
    }

    public long addRefundDAO(Refund refund, OrderNote note) throws Exception {
    em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();

    try {
        tx.begin();

        // Reattach and set references
        if (refund.getSale() != null) {
            Sale managedSale = em.getReference(Sale.class, refund.getSale().getIdSale());
            refund.setSale(managedSale);
        }

        if (refund.getServiceOrder() != null) {
            ServiceOrder managedOrder = em.getReference(ServiceOrder.class, refund.getServiceOrder().getIdServiceOrder());
            refund.setServiceOrder(managedOrder);
        }

        if (refund.getEmployee() != null) {
            Employee managedEmployee = em.getReference(Employee.class, refund.getEmployee().getIdEmployee());
            refund.setEmployee(managedEmployee);
        }

        if (refund.getCompany() != null) {
            Company managedCompany = em.find(Company.class, refund.getCompany().getIdCompany());
            refund.setCompany(managedCompany);
        }

        // Persist Refund first
        em.persist(refund);

        // Now update status AFTER refund be persisted
        if (refund.getSale() != null) {
            refund.getSale().setStatus(OrderStatus.REFUNDED);
        }

        if (refund.getServiceOrder() != null) {
            refund.getServiceOrder().setStatus(OrderStatus.REFUNDED);
        }

        // Persist OrderNote if present
        if (note != null) {
            note.setSale(refund.getSale());

            if (note.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, note.getEmployee().getIdEmployee());
                note.setEmployee(managedEmployee);
            }

            em.persist(note);
        }

        tx.commit();

        return refund.getIdRefund();

    } catch (Exception e) {
        if (tx.isActive()) {
            tx.rollback();
        }
        throw new BusinessException("Failed to add refund: " + e.getMessage(), e);
    } finally {
        em.close();
    }
}

}
