package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.DeviceDAO;
import com.pchouse.pchousestoremvn.models.Device;
import java.util.List;

public class DeviceController {

    private final DeviceDAO DEVICE_DAO = new DeviceDAO();

    // Fetches all devices from the database
    public List<Device> getAllDevice() {
        return DEVICE_DAO.getAllDeviceDAO();
    }

    // Adds a new device to the database
    public long addDevice(Device pDevice) {
        return DEVICE_DAO.addDeviceDAO(pDevice);
    }

    // Searches for a device using its serial number
    public Device searchDeviceBySerialNumber(String pSearch) {
        return DEVICE_DAO.searchDeviceBySerialNumber(pSearch);
    }

    // Searches for device brands based on a partial match
    public List<String> searchBrand(String pSearch) {
        return DEVICE_DAO.searchBrandDAO(pSearch);
    }

    // Searches for models of a particular brand based on a partial model name match
    public List<String> searchModel(String pBrand, String pModel) {
        return DEVICE_DAO.searchModelDAO(pBrand, pModel);
    }

    // Searches for serial numbers of a particular brand based on a partial match
    public List<String> searchSerialNumber(String pBrand, String pSerialNumber) {
        return DEVICE_DAO.searchSerialNumberDAO(pBrand, pSerialNumber);
    }
}
