package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.ProductServiceDAO;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.ProductService;
import java.util.List;

public class ProductServiceController {
    private final ProductServiceDAO productServiceDAO = new ProductServiceDAO();
    
    // Get all products or services for a specific company
    public List<ProductService> getAllProducts(Company company) {
        return productServiceDAO.getAllProductsDAO(company);
    }
    
    // Get products or services with minimum stock
    public List<ProductService> getMinStockProducts(Company company) {
        return productServiceDAO.getMinStockProductDAO(company);       
    }
    
    // Add a new product or service
    public long addProductService(ProductService productService) {
        return productServiceDAO.addProductServiceDAO(productService);
    }
    
    // Update an existing product or service
    public boolean updateProductService(ProductService productService) {
        return productServiceDAO.updateProductServiceDAO(productService);
    }
    
    // Delete a product or service
    public boolean deleteProductService(ProductService productService) {
        return productServiceDAO.deleteProductServiceDAO(productService);
    }
    
    // Search for products or services by a search string
    public List<ProductService> searchProductService(String search) {
        return productServiceDAO.searchProdServDAO(search);
    }
    
    // Order search for products or services
    public List<ProductService> searchOrderProductService(String search) {
        return productServiceDAO.orderSearchProdServDAO(search);
    }
    
    // Get details of a product or service by ID
    public ProductService getProductServiceById(long productId) {
        return productServiceDAO.getItemProdServDAO(productId);
    }
    
    // Check if a product or service already exists
    public long checkIfProductServiceExists(String search) {
        return productServiceDAO.checkExistProdServDAO(search);
    }
}
