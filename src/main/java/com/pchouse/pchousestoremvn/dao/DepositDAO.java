package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DepositDAO {

    public long addDepositDAO(Deposit deposit) {
        EntityManager em = JPAUtil.getEntityManager();
        long idDepositAdded = 0;
        try {
            em.getTransaction().begin();

            if (deposit.getServiceOrder() != null) {
                deposit.setServiceOrder(em.getReference(
                        ServiceOrder.class, deposit.getServiceOrder().getIdServiceOrder()));
            }

            if (deposit.getSale() != null) {
                deposit.setSale(em.getReference(
                        Sale.class, deposit.getSale().getIdSale()));
            }

            if (deposit.getServiceOrderPayment() != null) {
                deposit.setServiceOrderPayment(em.getReference(
                        ServiceOrderPayment.class, deposit.getServiceOrderPayment().getIdServiceOrderPayment()));
            }

            if (deposit.getSalePayment() != null) {
                deposit.setSalePayment(em.getReference(
                        SalePayment.class, deposit.getSalePayment().getIdSalePayment()));
            }

            if (deposit.getEmployee() != null) {
                deposit.setEmployee(em.getReference(
                        Employee.class, deposit.getEmployee().getIdEmployee()));
            }

            em.persist(deposit);
            em.getTransaction().commit();
            idDepositAdded = deposit.getIdDeposit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idDepositAdded;
    }

    public List<Deposit> getOrderDepositDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Deposit> orderDeposits = null;
        try {
            TypedQuery<Deposit> query = em.createQuery(
                    "SELECT d FROM Deposit d WHERE d.serviceOrder = :pOrder", Deposit.class);
            query.setParameter("pOrder", pOrder);
            orderDeposits = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderDeposits;
    }

    public List<Deposit> getSaleDepositDAO(Sale pSale) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Deposit> orderDeposits = null;
        try {
            TypedQuery<Deposit> query = em.createQuery(
                    "SELECT d FROM Deposit d WHERE d.sale = :pSale", Deposit.class);
            query.setParameter("pSale", pSale);
            orderDeposits = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderDeposits;
    }
}
