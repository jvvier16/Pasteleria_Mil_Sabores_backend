package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.DetalleBoleta;
import java.math.BigDecimal;

/**
 * DTO para representar un detalle de boleta en las respuestas
 */
public class DetalleBoletaDTO {
    
    private Long detalleId;
    private Long productoId;
    private String productoNombre;
    private String productoImagen;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
    
    // Constructores
    public DetalleBoletaDTO() {}
    
    public DetalleBoletaDTO(DetalleBoleta detalle) {
        this.detalleId = detalle.getDetalleId();
        this.cantidad = detalle.getCantidad();
        
        if (detalle.getProducto() != null) {
            this.productoId = detalle.getProducto().getProductoId();
            this.productoNombre = detalle.getProducto().getNombre();
            this.productoImagen = detalle.getProducto().getImagen();
            this.precioUnitario = detalle.getProducto().getPrecio();
            
            // Calcular subtotal
            if (this.precioUnitario != null && this.cantidad != null) {
                this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
            }
        }
    }
    
    // Getters y Setters
    public Long getDetalleId() {
        return detalleId;
    }
    
    public void setDetalleId(Long detalleId) {
        this.detalleId = detalleId;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public String getProductoNombre() {
        return productoNombre;
    }
    
    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }
    
    public String getProductoImagen() {
        return productoImagen;
    }
    
    public void setProductoImagen(String productoImagen) {
        this.productoImagen = productoImagen;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}




