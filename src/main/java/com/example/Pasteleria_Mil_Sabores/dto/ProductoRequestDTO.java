package com.example.Pasteleria_Mil_Sabores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * DTO para crear/actualizar productos
 * Usado en POST y PUT de /api/v2/productos
 */
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductoRequestDTO {
    
    @Schema(description = "Nombre del producto (obligatorio)", example = "Torta de Chocolate")
    private String nombre;
    
    @Schema(description = "Precio del producto (obligatorio)", example = "15000")
    private BigDecimal precio;
    
    @Schema(description = "Cantidad en stock", example = "10")
    private Integer stock;
    
    @Schema(description = "URL de la imagen del producto", example = "https://ejemplo.com/imagen.jpg")
    private String imagen;
    
    @Schema(description = "Descripción del producto", example = "Deliciosa torta de chocolate con cobertura de ganache")
    private String descripcion;
    
    @Schema(description = "ID de la categoría", example = "1")
    private Long categoriaId;

    // Constructores
    public ProductoRequestDTO() {}

    public ProductoRequestDTO(String nombre, BigDecimal precio, Integer stock, 
                              String imagen, String descripcion, Long categoriaId) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}

