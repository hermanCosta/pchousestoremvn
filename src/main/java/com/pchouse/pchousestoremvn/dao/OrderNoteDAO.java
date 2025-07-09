package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderNoteDAO {

    public long addOrderNoteDAO(OrderNote pOrderNote) {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderNoteAdded = 0;
        try {
            em.getTransaction().begin();

            // Ensure ServiceOrde is managed
            ServiceOrder managedOrder = em.find(ServiceOrder.class, pOrderNote.getServiceOrder().getIdServiceOrder());
            Employee manageEmployee = em.find(Employee.class, pOrderNote.getEmployee().getIdEmployee());

            // Assign managed references
            pOrderNote.setServiceOrder(managedOrder);
            pOrderNote.setEmployee(manageEmployee);

            // Persist the new ServiceOrderNote record
            em.persist(pOrderNote);
            em.getTransaction().commit();
            idOrderNoteAdded = pOrderNote.getIdServiceOrderNote();
        } catch (Exception e) {
            System.err.println("Error adding order note: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idOrderNoteAdded;
    }

    public List<OrderNote> getAllOrderNoteDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<OrderNote> listOrderNote = null;
        try {
            TypedQuery<OrderNote> query = em.createQuery("FROM ServiceOrderNote n WHERE n.serviceOrder = :pOrder ORDER BY n.created ASC", OrderNote.class);
            query.setParameter("pOrder", pOrder);
            listOrderNote = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving order notes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrderNote;
    }

    public Long checkExistingOrderNoteDAO(String pNote, ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        Long idOrderNote = null;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT n.idOrderNote FROM ServiceOrderNote n WHERE n.note = :pNote AND n.serviceOrder = :pOrder", Long.class);
            query.setParameter("pNote", pNote);
            query.setParameter("pOrder", pOrder);
            idOrderNote = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error checking existing order note: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return idOrderNote;
    }

    public List<OrderNote> searchOrderNoteDAO(ServiceOrder pOrder, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<OrderNote> listOrderNote = null;
        try {
            TypedQuery<OrderNote> query = em.createQuery("FROM ServiceOrderNote n WHERE n.serviceOrder = :pOrder AND n.note LIKE :pSearch", OrderNote.class);
            query.setParameter("pOrder", pOrder);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listOrderNote = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching order notes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrderNote;
    }
}
