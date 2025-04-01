package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Device;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DeviceDAO {

    public List<Device> getAllDeviceDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Device> listDevice = null;
        try {
            TypedQuery<Device> query = em.createQuery("SELECT d FROM Device d", Device.class);
            listDevice = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all devices: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listDevice;
    }

    public long addDeviceDAO(Device device) {
        EntityManager em = JPAUtil.getEntityManager();
        long idDeviceAdded = 0;
        try {
            em.getTransaction().begin();
            em.persist(device);
            em.getTransaction().commit();
            idDeviceAdded = device.getIdDevice(); // Ajuste conforme sua entidade
        } catch (Exception e) {
            System.err.println("Error adding device: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idDeviceAdded;
    }

    public Device searchDeviceBySerialNumber(String serialNumber) {
        EntityManager em = JPAUtil.getEntityManager();
        Device deviceItem = null;
        try {
            TypedQuery<Device> query = em.createQuery(
                "SELECT d FROM Device d WHERE d.serialNumber = :serialNumber", Device.class);
            query.setParameter("serialNumber", serialNumber);
            deviceItem = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error searching device by serial number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return deviceItem;
    }

    public List<String> searchBrandDAO(String search) {
        EntityManager em = JPAUtil.getEntityManager();
        List<String> listBrand = null;
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT DISTINCT d.brand FROM Device d WHERE d.brand LIKE :search", String.class);
            query.setParameter("search", "%" + search + "%");
            listBrand = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching brands: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listBrand;
    }

    public List<String> searchModelDAO(String brand, String model) {
        EntityManager em = JPAUtil.getEntityManager();
        List<String> listModel = null;
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT DISTINCT d.model FROM Device d WHERE d.brand = :brand AND d.model LIKE :model", String.class);
            query.setParameter("brand", brand);
            query.setParameter("model", "%" + model + "%");
            listModel = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching models: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listModel;
    }

    public List<String> searchSerialNumberDAO(String brand, String serialNumber) {
        EntityManager em = JPAUtil.getEntityManager();
        List<String> listSerialNumber = null;
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT DISTINCT d.serialNumber FROM Device d WHERE d.brand = :brand AND d.serialNumber LIKE :serialNumber", String.class);
            query.setParameter("brand", brand);
            query.setParameter("serialNumber", "%" + serialNumber + "%");
            listSerialNumber = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching serial numbers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listSerialNumber;
    }
}
