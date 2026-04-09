package com.GothWearShop.GothShop.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SellOrderRequestDTO {
        
    @NotNull(message = "La lista de items es obligatoria")
    @Size(min = 1, message = "Debe haber al menos un item en la compra")
    private List<SellOrderItemRequestDTO> items;
}
