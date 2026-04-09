package com.GothWearShop.GothShop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductRequestDTO {

     @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre del producto debe tener entre 2 y 150 caracteres")
    private String productname;

    @Size(max = 250, message = "La descripción del producto no debe exceder los 250 caracteres")
    private String productdescription;

    @NotNull(message = "El precio del producto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio del producto debe ser mayor que 0")
    private Long price;
}
