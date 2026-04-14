package com.GothWearShop.GothShop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.GothWearShop.GothShop.dto.CartRequestDTO;
import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.SellOrderItemRequestDTO;
import com.GothWearShop.GothShop.dto.SellOrderRequestDTO;
import com.GothWearShop.GothShop.entity.DetailCart;
import com.GothWearShop.GothShop.entity.Product;
import com.GothWearShop.GothShop.entity.ShoppingCart;
import com.GothWearShop.GothShop.repository.ProductRepository;
import com.GothWearShop.GothShop.repository.ShoppingCartRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;
    private final SellOrderService sellOrderService;

    
    public MessageResponseDTO addToCart(Long userId, CartRequestDTO request) {

        ShoppingCart cart = cartRepository.findByIdUser(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setIdUser(userId);
                    newCart.setCreationDate(LocalDateTime.now());
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<DetailCart> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId_product().equals(product.getId_product()))
                .findFirst();

       
        if (existingItem.isPresent()) {
            DetailCart item = existingItem.get();

            int newQuantity = item.getQuantity() + request.getQuantity();
            item.setQuantity(newQuantity);

            item.setAmount(
                    BigDecimal.valueOf(product.getPrice()) 
                            .multiply(BigDecimal.valueOf(newQuantity))
            );

        } else {
            DetailCart newItem = new DetailCart();
            newItem.setShoppingCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());

            newItem.setAmount(
                    BigDecimal.valueOf(product.getPrice()) 
                            .multiply(BigDecimal.valueOf(request.getQuantity()))
            );

            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Producto agregado al carrito");
        return response;
    }

    
    public ShoppingCart getCart(Long userId) {
        return cartRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("Carrito vacío"));
    }

    
    public MessageResponseDTO removeProduct(Long userId, Long productId) {

        ShoppingCart cart = cartRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        cart.getItems().removeIf(item ->
                item.getProduct().getId_product().equals(productId));

        cartRepository.save(cart);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("Producto eliminado del carrito");
        return response;
    }

    
    public MessageResponseDTO checkout(Long userId) {

        ShoppingCart cart = cartRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("Carrito vacío"));

        SellOrderRequestDTO request = new SellOrderRequestDTO();

        List<SellOrderItemRequestDTO> items = cart.getItems().stream()
                .map(item -> {
                    SellOrderItemRequestDTO dto = new SellOrderItemRequestDTO();
                    dto.setProductId(item.getProduct().getId_product());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                }).toList();

        request.setItems(items);


        MessageResponseDTO response =
                sellOrderService.createPurchase(request, userId);

       
        cartRepository.delete(cart);

        return response;
    }
}