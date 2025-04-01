package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonDAO {

    public List<Person> getAllPersonDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Person> persons = null;
        try {
            persons = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving Persons: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return persons;
    }

    public long addPersonDAO(Person pPerson) {
        EntityManager em = JPAUtil.getEntityManager();
        long idPersonAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(pPerson);
            em.getTransaction().commit();
            idPersonAdded = pPerson.getIdPerson(); // Obtendo o ID gerado
        } catch (Exception e) {
            System.err.println("Error adding Person: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idPersonAdded;
    }

    public boolean updatePersonDAO(Person pPerson) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pPerson);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error updating Person: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deletePersonDAO(Person pPerson) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Person personToRemove = em.find(Person.class, pPerson.getIdPerson());
            if (personToRemove != null) {
                em.remove(personToRemove);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting Person: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Person> searchPersonDAO(String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Person> persons = null;
        try {
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE " +
                            "LOWER(p.firstName) LIKE LOWER(:pSearch) OR " +
                            "LOWER(p.lastName) LIKE LOWER(:pSearch) OR " +
                            "p.contactNo LIKE :pSearch OR " +
                            "LOWER(p.email) LIKE LOWER(:pSearch)",
                    Person.class);
            query.setParameter("pSearch", "%" + pSearch + "%");
            persons = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching Person: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return persons;
    }

    public Person searchPersonByContactNoDAO(String pContactNo) {
        EntityManager em = JPAUtil.getEntityManager();
        Person person = null;
        try {
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.contactNo = :pContactNo", Person.class);
            query.setParameter("pContactNo", pContactNo);
            person = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error searching Person by ContactNo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return person;
    }
}
