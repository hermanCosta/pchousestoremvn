package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
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
            em.persist(deposit);
            em.getTransaction().commit();
            idDepositAdded = deposit.getIdDeposit(); // Ajuste conforme sua entidade
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idDepositAdded;
    }

    public List<Deposit> getOrderDepositDAO(ServiceOrder order) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Deposit> orderDeposits = null;
        try {
            TypedQuery<Deposit> query = em.createQuery(
                "SELECT d FROM Deposit d WHERE d.order = :order", Deposit.class);
            query.setParameter("order", order);
            orderDeposits = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderDeposits;
    }
}
