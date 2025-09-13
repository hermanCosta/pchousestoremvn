package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Fault;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServiceOrderFaultDAO {

public long addOrderFaultDAO(ServiceOrderFault pOrderFault) {
    EntityManager em = JPAUtil.getEntityManager();
    long idOrderFaultAdded = 0;
    try {
        em.getTransaction().begin();

        // Ensure ServiceOrder and Fault are managed
        Fault managedFault = em.find(Fault.class, pOrderFault.getFault().getIdFault());
        ServiceOrder managedOrder = em.find(ServiceOrder.class, pOrderFault.getServiceOrder().getIdServiceOrder());

        pOrderFault.setFault(managedFault);
        pOrderFault.setServiceOrder(managedOrder);

        // Now persist the ServiceOrderFault
        em.persist(pOrderFault);
        em.getTransaction().commit();
        idOrderFaultAdded = pOrderFault.getIdServiceOrderFault();
    } catch (Exception e) {
        System.err.println("Error adding order fault: " + e.getMessage());
        e.printStackTrace();
        em.getTransaction().rollback();
    } finally {
        em.close();
    }
    return idOrderFaultAdded;
}

    public List<ServiceOrderFault> getOrderFaultDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrderFault> listOrderFault = null;
        try {
            TypedQuery<ServiceOrderFault> query = em.createQuery(
                "FROM ServiceOrderFault f WHERE f.serviceOrder = :pOrder", ServiceOrderFault.class);
            query.setParameter("pOrder", pOrder);
            listOrderFault = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving order faults: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrderFault;
    }

    public boolean updateOrderFaultDAO(ServiceOrderFault pOrderFault) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            em.merge(pOrderFault);
            em.getTransaction().commit();
            success = true;
        } catch (Exception e) {
            System.err.println("Error updating order fault: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return success;
    }

    public long deleteOrderFaultDAO(long pIdOrderFault) {
        EntityManager em = JPAUtil.getEntityManager();
        long resultDelete = 0;
        try {
            em.getTransaction().begin();
            resultDelete = em.createQuery(
                "DELETE FROM ServiceOrderFault f WHERE f.idOrderFault = :pIdOrderFault")
                .setParameter("pIdOrderFault", pIdOrderFault)
                .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting order fault: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return resultDelete;
    }
}
