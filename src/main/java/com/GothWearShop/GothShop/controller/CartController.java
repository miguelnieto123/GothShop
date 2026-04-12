package com.GothWearShop.GothShop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GothWearShop.GothShop.dto.CartRequestDTO;
import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // agregar productos al carrito
    @PostMapping("/add")
    public ResponseEntity<MessageResponseDTO> addToCart(
            @RequestBody CartRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            String role = (String) httpRequest.getAttribute("role");

            if (!"cliente".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("Solo los clientes pueden usar el carrito");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            Long userId = (Long) httpRequest.getAttribute("id");

            return ResponseEntity.ok(
                    cartService.addToCart(userId, request));

        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ver lo que hay en el carrito
    @GetMapping
    public ResponseEntity<?> getCart(HttpServletRequest httpRequest) {
        try {
            Long userId = (Long) httpRequest.getAttribute("id");
            return ResponseEntity.ok(cartService.getCart(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // eliminar productos
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<MessageResponseDTO> removeProduct(
            @PathVariable Long productId,
            HttpServletRequest httpRequest) {

        try {
            String role = (String) httpRequest.getAttribute("role");

            if (!"cliente".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("Solo los clientes pueden modificar el carrito");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            Long userId = (Long) httpRequest.getAttribute("id");

            return ResponseEntity.ok(
                    cartService.removeProduct(userId, productId));

        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // checkout de los productos en el carrito
    @PostMapping("/checkout")
    public ResponseEntity<MessageResponseDTO> checkout(
            HttpServletRequest httpRequest) {

        try {
            String role = (String) httpRequest.getAttribute("role");

            if (!"cliente".equals(role)) {
                MessageResponseDTO error = new MessageResponseDTO();
                error.setMessage("Solo los clientes pueden comprar");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            Long userId = (Long) httpRequest.getAttribute("id");

            return ResponseEntity.ok(
                    cartService.checkout(userId));

        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
