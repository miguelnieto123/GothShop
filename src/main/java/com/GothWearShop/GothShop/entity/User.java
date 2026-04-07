package com.GothWearShop.GothShop.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id_user ;

    @Column(name = "username")
    private String username ;

    @Column(name = "email")
    private String email;

    @Column(name = "userpassword")
    private String userpassword;

    @Column(name = "userstatus")
    private Boolean userstatus;

    @Column(name = "register_date")
    private LocalDate register_date;

    @Column(name = "id_rol")
    private Long id_rol;

}