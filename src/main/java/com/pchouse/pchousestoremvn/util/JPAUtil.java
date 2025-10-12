package com.pchouse.pchousestoremvn.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory FACTORY = 
        Persistence.createEntityManagerFactory("pcHouseStorePU");

    static {
        System.out.println("JPAUtil: Initializing EntityManagerFactory for pcHouseStorePU");
    }

    public static EntityManager getEntityManager() {
        try {
            System.out.println("JPAUtil: Creating new EntityManager");
            return FACTORY.createEntityManager();
        } catch (Exception e) {
            System.out.println("JPAUtil: Error creating EntityManager - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            System.out.println("JPAUtil: Closing EntityManagerFactory");
            FACTORY.close();
        }
    }
}
