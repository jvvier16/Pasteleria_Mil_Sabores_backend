package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;

/**
 * DTO para Categoria - Usado en API v2 (pública)
 */
public class CategoriaDTO {
    
    private Long categoriaId;
    private String nombre;
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

