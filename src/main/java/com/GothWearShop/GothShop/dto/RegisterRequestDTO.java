package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    
     private String name;
     private String email;
     private String password;
     private Long id_rol;

    
}
