package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class LoginResponseDTO extends MessageResponseDTO {
    private String jwt;
}