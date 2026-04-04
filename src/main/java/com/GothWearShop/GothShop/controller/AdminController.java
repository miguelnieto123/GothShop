package com.GothWearShop.GothShop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    // CRUD PRODUCTOS
    @PostMapping("/productos")
    public void crearProducto() {}

    @PutMapping("/productos/{id}")
    public void editarProducto() {}

    @DeleteMapping("/productos/{id}")
    public void eliminarProducto() {}

    // CRUD USUARIOS
    @PostMapping("/usuarios")
    public void crearUsuario() {}

    @DeleteMapping("/usuarios/{id}")
    public void eliminarUsuario() {}
}