package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.ProductService;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SaleProdServDAO {

    public long addSaleProdServDAO(SaleProdServ pSaleProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long idSaleProdServAdded = 0;
        try {
            em.getTransaction().begin();

            // Ensure Sale and ProductService are managed
            Sale managedSale = em.find(Sale.class, pSaleProdServ.getSale().getIdSale());
            ProductService managedProdServ = em.find(ProductService.class, pSaleProdServ.getProdServ().getIdProductService());

            // Assign managed references
            pSaleProdServ.setSale(managedSale);
            pSaleProdServ.setProdServ(managedProdServ);

            // Persist the new SaleProdServ record
            em.persist(pSaleProdServ);
            em.getTransaction().commit();
            idSaleProdServAdded = pSaleProdServ.getIdSaleProdServ();
        } catch (Exception e) {
            System.err.println("Error adding SaleProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idSaleProdServAdded;
    }

    public List<SaleProdServ> getSaleProdServDAO(Sale pSale) {
        EntityManager em = JPAUtil.getEntityManager();
        List<SaleProdServ> listSaleProdServ = null;
        try {
            TypedQuery<SaleProdServ> query = em.createQuery(
                    "FROM SaleProdServ s WHERE s.sale = :pSale", SaleProdServ.class);
            query.setParameter("pSale", pSale);
            listSaleProdServ = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving SaleProdServ: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listSaleProdServ;
    }

    public long deleteSaleProdServDAO(long pIdSaleProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        long result = 0;
        try {
            em.getTransaction().begin();
            result = em.createQuery("DELETE FROM SaleProdServ s WHERE s.idSaleProdServ = :pIdSaleProdServ")
                    .setParameter("pIdSaleProdServ", pIdSaleProdServ)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error deleting SaleProdServ: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return result;
    }

    public List<RefurbSale> getRefurbSaleDAO(Sale pSale) {
        EntityManager em = JPAUtil.getEntityManager();
        List<RefurbSale> listRefurbSale = null;
        try {
            TypedQuery<RefurbSale> query = em.createQuery(
                    "FROM RefurbSale r WHERE r.sale = :pSale", RefurbSale.class);
            query.setParameter("pSale", pSale);
            listRefurbSale = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving RefurbSale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listRefurbSale;
    }
}
