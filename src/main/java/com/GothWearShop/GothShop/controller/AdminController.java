package com.GothWearShop.GothShop.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GothWearShop.GothShop.dto.LoginRequestDTO;
import com.GothWearShop.GothShop.dto.LoginResponseDTO;
import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.ProductRequestDTO;
import com.GothWearShop.GothShop.dto.RefreshTokenResponseDTO;
import com.GothWearShop.GothShop.dto.RegisterRequestDTO;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.service.AuthService;
import com.GothWearShop.GothShop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    
   private final AuthService authService;
    
   private final ProductService productService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<MessageResponseDTO> register1(@RequestBody ProductRequestDTO request) {
        try {
            MessageResponseDTO response = productService.register1(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/GetProducts")
    public ResponseEntity<Optional<Product>> getProducts() {
    return ResponseEntity.ok(productService.getActiveProducts());
}

     @PostMapping("/registerProductsAdmin")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            MessageResponseDTO response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String token = authHeader.substring(7);
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.setMessage("Token expired");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}

    

