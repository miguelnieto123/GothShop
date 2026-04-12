package com.GothWearShop.GothShop.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "detailcart")
@Data
public class DetailCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detailcart")
    private Long idDetailCart;

    @ManyToOne
    @JoinColumn(name = "id_shoppingcart", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // getters y setters
}