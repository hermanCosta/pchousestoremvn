package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CompanyDAO {

    public List<Company> getAllCompaniesDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Company> listCompanies = null;
        try {
            listCompanies = em.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listCompanies;
    }

    public Company getCompanyDAO(String name, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        Company company = null;
        try {
            // Debug logging
            System.out.println("Attempting login for company: " + name);
            System.out.println("Password hash length: " + (password != null ? password.length() : "null"));
            
            TypedQuery<Company> query = em.createQuery(
                "SELECT c FROM Company c WHERE c.name = :name AND c.password = :password", Company.class);
            query.setParameter("name", name);
            query.setParameter("password", password);
            company = query.getSingleResult();
            
            System.out.println("Login successful for company: " + name);
        } catch (Exception e) {
            System.out.println("CompanyDAO Login Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return company;
    }
}
