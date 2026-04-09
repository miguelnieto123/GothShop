package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    
     private String username;
     private String email;
     private String userpassword;
     private Long id_rol;

    
}
