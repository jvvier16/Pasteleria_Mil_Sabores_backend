package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import java.math.BigDecimal;

/**
 * DTO para Producto con estadísticas detalladas (v2)
 */
public class ProductoDTO {
    
    private Long productoId;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private String imagen;
    private String descripcion;
    private Long categoriaId;
    private String categoriaNombre;
    
    // Estadísticas avanzadas (v2)
    private Integer totalVendidos;
    private Boolean disponible;
    private String estadoStock;

    public ProductoDTO() {}

    public ProductoDTO(Producto producto) {
        this.productoId = producto.getProductoId();
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.stock = producto.getStock();
        this.imagen = producto.getImagen();
        this.descripcion = producto.getDescripcion();
        
        // Cargar categoría de forma segura (puede ser null o lazy)
        try {
            if (producto.getCategoria() != null) {
                this.categoriaId = producto.getCategoria().getCategoriaId();
                this.categoriaNombre = producto.getCategoria().getNombre();
            }
        } catch (Exception e) {
            // Ignorar si la categoría no está cargada (lazy)
            this.categoriaId = null;
            this.categoriaNombre = null;
        }
        
        // Calcular estadísticas básicas
        this.disponible = producto.getStock() != null && producto.getStock() > 0;
        this.estadoStock = calcularEstadoStock(producto.getStock());
        
        // Evitar cargar detalles lazy - usar valor por defecto
        this.totalVendidos = 0;
    }

    private String calcularEstadoStock(Integer stock) {
        if (stock == null || stock == 0) return "SIN_STOCK";
        if (stock < 5) return "CRITICO";
        if (stock < 20) return "BAJO";
        return "DISPONIBLE";
    }

    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

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

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Integer getTotalVendidos() {
        return totalVendidos;
    }

    public void setTotalVendidos(Integer totalVendidos) {
        this.totalVendidos = totalVendidos;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getEstadoStock() {
        return estadoStock;
    }

    public void setEstadoStock(String estadoStock) {
        this.estadoStock = estadoStock;
    }
}

