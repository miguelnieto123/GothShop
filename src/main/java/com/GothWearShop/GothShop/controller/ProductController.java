package com.GothWearShop.GothShop.controller;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.ProductRequestDTO;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/create")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/registerProduct")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody ProductRequestDTO request) {
        try {
            MessageResponseDTO response = productService.register1(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<Optional<Product>> getProducts() {
    return ResponseEntity.ok(productService.getActiveProducts());

    
    
}
}
