package com.example.Pasteleria_Mil_Sabores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para representar un item del carrito de compras
 */
@Schema(description = "Item del carrito de compras")
public class ItemCarritoDTO {
    
    @Schema(description = "ID del producto", example = "1")
    @NotNull(message = "El ID del producto es requerido")
    private Long productoId;
    
    @Schema(description = "Cantidad a comprar", example = "2")
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    // Constructores
    public ItemCarritoDTO() {}
    
    public ItemCarritoDTO(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
