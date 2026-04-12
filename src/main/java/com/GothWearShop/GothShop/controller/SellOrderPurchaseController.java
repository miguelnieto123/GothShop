package com.GothWearShop.GothShop.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.SellOrderRequestDTO;
import com.GothWearShop.GothShop.entity.order;
import com.GothWearShop.GothShop.service.SellOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class SellOrderPurchaseController {

    private final SellOrderService purchaseService;

    @PostMapping("/purchase")
    public ResponseEntity<MessageResponseDTO> createPurchase(@Valid @RequestBody SellOrderRequestDTO request, HttpServletRequest httpRequest) {
        try {
            String rol_name = (String) httpRequest.getAttribute("role");
            Long userId = (Long) httpRequest.getAttribute("id_user");
            return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(request, userId));
        } catch (Exception e) {
            e.printStackTrace();
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/Historial")
    public ResponseEntity<List<order>> getMyPurchases(HttpServletRequest httpRequest) {
        try {
            Long userId = (Long) httpRequest.getAttribute("id_user");
            return ResponseEntity.ok(purchaseService.getMyPurchases(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<order>> getAllPurchases(HttpServletRequest httpRequest) {
        try {
            String role = (String) httpRequest.getAttribute("id_rol");
            if (!"admin".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok(purchaseService.getAllPurchases());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
