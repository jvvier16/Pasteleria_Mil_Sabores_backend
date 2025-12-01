package com.example.Pasteleria_Mil_Sabores.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DETALLE_BOLETA")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    @SequenceGenerator(name = "detalle_seq", sequenceName = "DETALLE_BOLETA_SEQ", allocationSize = 1)
    @Column(name = "DETALLE_ID")
    private Long detalleId;

    // Relación con Producto - FK explícita
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCTO_ID", referencedColumnName = "PRODUCTO_ID")
    private Producto producto;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    // Relación con Boleta - FK explícita
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOLETA_ID", referencedColumnName = "BOLETA_ID")
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
                ", cantidad=" + cantidad +
                '}';
    }
}