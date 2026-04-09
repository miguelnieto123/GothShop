package com.GothWearShop.GothShop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sellorder")
public class order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id_order;

    @Column(name = "id_user", nullable = false)
    private Long userId;

    @Column(name = "id_product")
    private BigDecimal total;

    @Column(name = "orderdate")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "id_order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetailOrder> items;
}