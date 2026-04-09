package com.GothWearShop.GothShop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.SellOrderItemRequestDTO;
import com.GothWearShop.GothShop.dto.SellOrderRequestDTO;
import com.GothWearShop.GothShop.entity.DetailOrder;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.entity.order;
import com.GothWearShop.GothShop.repository.ProductRepository;
import com.GothWearShop.GothShop.repository.sellorderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellOrderService {
    
    private final sellorderRepository purchaseRepository;

    private final ProductRepository productRepository;

    @Transactional
    public MessageResponseDTO createPurchase(SellOrderRequestDTO request, Long UserId) {
        order purchase = new order();
        purchase.setUserId(UserId);
        purchase.setOrderDate(LocalDateTime.now());

        List<DetailOrder> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (SellOrderItemRequestDTO itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + itemReq.getProductId()));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException(
                    "Stock insuficiente para: " + product.getProductname() + " | Disponible " + product.getStock());
            }

            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);

            DetailOrder item = new DetailOrder();
            item.setId_order(purchase);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitary_Price(product.getPrice());
            items.add(item);

            total = total.add(
             BigDecimal.valueOf(product.getPrice())
             .multiply(BigDecimal.valueOf(itemReq.getQuantity()))
);
        }

        purchase.setItems(items);
        purchase.setTotal(total);
        purchaseRepository.save(purchase);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Compra realizada eitosamente. Total: $" + total);
        return response;
    }

    public List<order> getMyPurchases(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    public List<order> getAllPurchases() {
        return purchaseRepository.findAll();
    }
}
