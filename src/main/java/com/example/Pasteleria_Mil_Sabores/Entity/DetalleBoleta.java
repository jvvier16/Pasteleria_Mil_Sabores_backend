package com.example.Pasteleria_Mil_Sabores.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_BOLETA")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detalleId;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "nombre", length = 200)
    private String nombre; // copia del nombre del producto al momento de la venta

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio", precision = 12, scale = 2, nullable = false)
    private BigDecimal precio; // precio unitario en el momento de la venta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boleta_id")
    private Boleta boleta;

    // Getters y Setters

    public Long getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(Long detalleId) {
        this.detalleId = detalleId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boleta getBoleta() {
        return boleta;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    @Override
    public String toString() {
        return "DetalleBoleta{" +
                "detalleId=" + detalleId +
                ", producto=" + (producto != null ? producto.getProductoId() : null) +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}