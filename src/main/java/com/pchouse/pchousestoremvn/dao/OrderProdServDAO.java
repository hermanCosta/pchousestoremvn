package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.ProductService;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderProdServDAO {

    public long addOrderProdServDAO(ServiceOrderProdServ pOrderProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderProdServAdded = 0;
        try {
            em.getTransaction().begin();

            // Ensure ServiceOrder and ProductService are managed
            ServiceOrder managedOrder = em.find(ServiceOrder.class, pOrderProdServ.getServiceOrder().getIdServiceOrder());
            ProductService managedProdServ = em.find(ProductService.class, pOrderProdServ.getProdServ().getIdProductService());

            // Assign managed references
            pOrderProdServ.setServiceOrder(managedOrder);
            pOrderProdServ.setProdServ(managedProdServ);

            // Persist the new ServiceOrderProdServ record
            em.persist(pOrderProdServ);
            em.getTransaction().commit();
            idOrderProdServAdded = pOrderProdServ.getIdServiceOrderProdServ();
        } catch (Exception e) {
            System.err.println("Error adding OrderProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idOrderProdServAdded;
    }

    public List<ServiceOrderProdServ> getOrderProdServDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrderProdServ> listOrderProdServ = null;
        try {
            TypedQuery<ServiceOrderProdServ> query = em.createQuery(
                    "FROM ServiceOrderProdServ o WHERE o.serviceOrder = :pOrder", ServiceOrderProdServ.class);
            query.setParameter("pOrder", pOrder);
            listOrderProdServ = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving OrderProdServ: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrderProdServ;
    }

    public long deleteOrderProdServDAO(long pIdOrderProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long result = 0;
        try {
            em.getTransaction().begin();
            result = em.createQuery("DELETE FROM ServiceOrderProdServ o WHERE o.idOrderProdServ = :pIdOrderProdServ")
                    .setParameter("pIdOrderProdServ", pIdOrderProdServ)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting OrderProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return result;
    }
}
