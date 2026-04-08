package com.GothWearShop.GothShop.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.ProductRequestDTO;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public ResponseEntity<Product>getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO>createProduct(@Valid @RequestBody ProductRequestDTO request, HttpServletRequest httpRequest) {
        try {
            String role = (String) httpRequest.getAttribute("role");
            if (!"admin".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("No tienes permisos para realizar esta acción");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO>updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request, HttpServletRequest httpRequest) {
        try {
            String role = (String) httpRequest.getAttribute("role");
            if (!"admin".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("No tienes permisos para realizar esta acción");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            return ResponseEntity.ok(productService.updateProduct(id, request));
        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteProduct(@PathVariable Long id, HttpServletRequest httpRequest) {
        try {
            String role = (String) httpRequest.getAttribute("role");
            if (!"admin".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("No tienes permisos para realizar esta acción");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            return ResponseEntity.ok(productService.deleteProduct(id));
        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
    




