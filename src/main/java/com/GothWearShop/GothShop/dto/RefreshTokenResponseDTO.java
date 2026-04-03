package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class RefreshTokenResponseDTO {
    private String jwt;
    private String message;
}
