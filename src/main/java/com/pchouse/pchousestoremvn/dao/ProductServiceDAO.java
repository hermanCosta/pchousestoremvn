package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.ProductService;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductServiceDAO {

    public List<ProductService> getAllProductsDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ProductService> products = null;
        try {
            TypedQuery<ProductService> query = em.createQuery(
                    "SELECT ps FROM ProductService ps WHERE ps.company = :pCompany ORDER BY ps.idProductService ASC",
                    ProductService.class);
            query.setParameter("pCompany", pCompany);
            products = query.getResultList();
        } finally {
            em.close();
        }
        return products;
    }

    public List<ProductService> getMinStockProductDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ProductService> products = null;
        try {
            TypedQuery<ProductService> query = em.createQuery(
                    "SELECT ps FROM ProductService ps WHERE ps.category = 'PRODUCT' AND ps.qty <= 2 AND ps.company = :pCompany",
                    ProductService.class);
            query.setParameter("pCompany", pCompany);
            products = query.getResultList();
        } finally {
            em.close();
        }
        return products;
    }

    public long addProductServiceDAO(ProductService pProductService) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pProductService);
            em.getTransaction().commit();
            return pProductService.getIdProductService();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error adding ProductService: " + e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    public boolean updateProductServiceDAO(ProductService pProductService) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pProductService);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error updating ProductService: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deleteProductServiceDAO(ProductService pProductService) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ProductService entity = em.find(ProductService.class, pProductService.getIdProductService());
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error deleting ProductService: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public List<ProductService> searchProdServDAO(String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ProductService> products = null;
        try {
            TypedQuery<ProductService> query = em.createQuery(
                    "SELECT ps FROM ProductService ps WHERE " +
                            "LOWER(ps.prodServName) LIKE LOWER(:pSearch) " +
                            "OR CAST(ps.price AS string) LIKE :pSearch " +
                            "OR LOWER(ps.note) LIKE LOWER(:pSearch) " +
                            "AND ps.company = :pCompany ORDER BY ps.prodServName",
                    ProductService.class);
            query.setParameter("pSearch", "%" + pSearch + "%");
            query.setParameter("pCompany", CommonSetting.COMPANY);
            products = query.getResultList();
        } finally {
            em.close();
        }
        return products;
    }

    public List<ProductService> orderSearchProdServDAO(String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ProductService> products = null;
        try {
            TypedQuery<ProductService> query = em.createQuery(
                    "SELECT ps FROM ProductService ps WHERE " +
                            "LOWER(ps.prodServName) LIKE LOWER(:pSearch) " +
                            "OR CAST(ps.price AS string) LIKE :pSearch " +
                            "OR LOWER(ps.note) LIKE LOWER(:pSearch) " +
                            "ORDER BY ps.prodServName",
                    ProductService.class);
            query.setParameter("pSearch", "%" + pSearch + "%");
            query.setMaxResults(5);
            products = query.getResultList();
        } finally {
            em.close();
        }
        return products;
    }

    public ProductService getItemProdServDAO(long pIdProdServ) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(ProductService.class, pIdProdServ);
        } finally {
            em.close();
        }
    }

    public long checkExistProdServDAO(String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        Long idExistProdServ = 0L;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT ps.idProductService FROM ProductService ps WHERE ps.prodServName = :pSearch",
                    Long.class);
            query.setParameter("pSearch", pSearch);
            idExistProdServ = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error checking existence of ProductService: " + e.getMessage());
        } finally {
            em.close();
        }
        return idExistProdServ != null ? idExistProdServ : 0L;
    }
}
