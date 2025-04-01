package com.pchouse.pchousestoremvn.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory FACTORY = 
        Persistence.createEntityManagerFactory("pcHouseStorePU");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
