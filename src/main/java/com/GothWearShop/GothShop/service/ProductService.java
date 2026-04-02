package com.GothWearShop.GothShop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.ProductRequestDTO;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.repository.ProductRepository;



public class ProductService {

     @Autowired

    private ProductRepository productRepository;

    public MessageResponseDTO register(ProductRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Registro exitoso");

        if (productRepository.findByStatusTrue().isPresent()) {
            throw new RuntimeException("Este nombre de usuario ya está en uso");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        return response;
    }

     public Optional<Product> getActiveProducts() {
        
    return productRepository.findByStatusTrue();
}
}
