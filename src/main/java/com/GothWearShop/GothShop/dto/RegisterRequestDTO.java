package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    
     private String name;
     private String email;
     private String password;
     private boolean id_rol;
     public Long getId_rol() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId_rol'");
     }
}
