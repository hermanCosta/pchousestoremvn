package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceOrderPaymentDAO {

    public long addOrderPaymentDAO(List<ServiceOrderPayment> pOrderPayments, OrderNote note) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        ServiceOrderPayment lastPayment = null;

        try {
            em.getTransaction().begin();

            if (pOrderPayments != null && !pOrderPayments.isEmpty()) {
                for (ServiceOrderPayment payment : pOrderPayments) {
                    if (payment.getServiceOrder() != null) {
                        // Reattach ServiceOrder properly
                        ServiceOrder managedServiceOrder = em.getReference(
                                ServiceOrder.class,
                                payment.getServiceOrder().getIdServiceOrder()
                        );
                        payment.setServiceOrder(managedServiceOrder);
                    }

                    em.persist(payment);
                    lastPayment = payment;
                }

                // Update status AFTER persisting last payment
                if (lastPayment != null && lastPayment.getServiceOrder() != null) {
                    lastPayment.getServiceOrder().setStatus(OrderStatus.PICKED);
                    lastPayment.getServiceOrder().setPicked(new Date());
                }
            }

            // Persist OrderNote
            if (note != null && lastPayment != null) {
                note.setServiceOrder(lastPayment.getServiceOrder());

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
            return lastPayment != null ? lastPayment.getIdServiceOrderPayment() : -1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new BusinessException("Failed to add payment: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<ServiceOrderPayment> getServiceOrderPaymentDAO(ServiceOrder pServiceOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrderPayment> payments = new ArrayList<>();

        try {
            em.getTransaction().begin();

            payments = em.createQuery(
                    "SELECT sp FROM ServiceOrderPayment sp WHERE sp.serviceOrder.idServiceOrder = :idServiceOrder",
                    ServiceOrderPayment.class
            )
                    .setParameter("idServiceOrder", pServiceOrder.getIdServiceOrder())
                    .getResultList();

            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error fetching service order payments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return payments;
    }

    public List<ServiceOrderPayment> getAllByDate(Date date) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrderPayment> list = null;
        try {
            TypedQuery<ServiceOrderPayment> query = em.createQuery(
                    "SELECT s FROM ServiceOrderPayment s WHERE s.serviceOrder.company = :company AND DATE(s.dtTransaction) = DATE(:date)",
                    ServiceOrderPayment.class
            );
            query.setParameter("date", date);
            query.setParameter("company", CommonSetting.COMPANY);
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return list;
    }

}
