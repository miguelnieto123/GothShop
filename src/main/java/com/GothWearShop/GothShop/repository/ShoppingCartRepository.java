package com.GothWearShop.GothShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GothWearShop.GothShop.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByIdUser(Long idUser);
}
