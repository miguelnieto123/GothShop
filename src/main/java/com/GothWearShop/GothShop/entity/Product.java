package com.GothWearShop.GothShop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long id_product;

    @Column(name = "productname")
    private String productname;

    @Column(name = "productdescription")
    private String prductdescription;

    @Column(name = "price")
    private Long price;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "status")
    private Boolean status;


}

