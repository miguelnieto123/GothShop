package com.GothWearShop.GothShop.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.GothWearShop.GothShop.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "name")
    private String name ;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "register_date")
    private LocalDate register_date;

    @Column(name = "id_rol")
    private Long id_rol;

}