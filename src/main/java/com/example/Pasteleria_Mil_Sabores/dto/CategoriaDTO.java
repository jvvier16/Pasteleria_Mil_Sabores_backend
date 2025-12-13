package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para Categoria - Usado en API v1 y v2
 */
@Schema(description = "Datos de una categoría de productos")
public class CategoriaDTO {
    
    @Schema(description = "ID de la categoría (auto-generado en creación)", example = "1")
    private Long categoriaId;
    
    @Schema(description = "Nombre de la categoría", example = "Tortas")
    private String nombre;
    
    @Schema(description = "Descripción de la categoría", example = "Tortas de diferentes sabores y tamaños")
    private String descripcion;

    public CategoriaDTO() {}

    public CategoriaDTO(Categoria categoria) {
        this.categoriaId = categoria.getCategoriaId();
        this.nombre = categoria.getNombre();
        this.descripcion = categoria.getDescripcion();
    }

    // Getters y Setters
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
