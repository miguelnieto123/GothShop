package com.GothWearShop.GothShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.GothWearShop.GothShop.entity.Rol;
import com.GothWearShop.GothShop.enums.RoleName;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
     Optional<Rol> findByName(RoleName nombre);
}