package com.GothWearShop.GothShop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.ProductRequestDTO;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.repository.ProductRepository;


@Service
public class ProductService {

     @Autowired

    private ProductRepository productRepository;

     public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public MessageResponseDTO register1(ProductRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Registro exitoso");

        if (productRepository.findByStatusTrue(true).isPresent()) {
            throw new RuntimeException("Este nombre de usuario ya está en uso");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        return response;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public MessageResponseDTO createProduct(ProductRequestDTO request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
    

        productRepository.save(product);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Producto creado exitosamente");
        return response;
    }

    public MessageResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Producto actualizado exitosamente");
        return response;
    }

    public MessageResponseDTO deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Producto eliminado exitosamente");
        return response;
    }
}



