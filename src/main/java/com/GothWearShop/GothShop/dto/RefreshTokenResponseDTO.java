package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class RefreshTokenResponseDTO {
    private String jwt;

    public void setMessage(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMessage'");
    }
}
