package com.GothWearShop.GothShop.dto;

import lombok.Data;

@Data
public class CartRequestDTO {

    private Long productId;
    private int quantity;

    
}
