package com.GothWearShop.GothShop.service;

import java.util.List;

import com.GothWearShop.GothShop.entity.Product;

public class ProductService {
    public List<Product> getActiveProducts() {
    List<Product> products = productRepository.findByStatusTrue();
    
    if (products.isEmpty()) {
        throw new RuntimeException("No hay productos disponibles");
    }
    
    return products;
}

}
