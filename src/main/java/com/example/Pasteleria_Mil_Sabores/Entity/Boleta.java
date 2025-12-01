package com.example.Pasteleria_Mil_Sabores.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOLETA")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boletaId;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Relación con Usuario (cliente). Aprovecha la entidad Usuario existente.
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario cliente;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> items = new ArrayList<>();

    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "estado", length = 50)
    private String estado; // p.ej. "pendiente", "procesado", "enviado"

    // Getters y Setters

    public Long getBoletaId() {
        return boletaId;
    }

    public void setBoletaId(Long boletaId) {
        this.boletaId = boletaId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public List<DetalleBoleta> getItems() {
        return items;
    }

    public void setItems(List<DetalleBoleta> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void addItem(DetalleBoleta detalle) {
        detalle.setBoleta(this);
        this.items.add(detalle);
    }

    public void removeItem(DetalleBoleta detalle) {
        detalle.setBoleta(null);
        this.items.remove(detalle);
    }

    @Override
    public String toString() {
        return "Boleta{" +
                "boletaId=" + boletaId +
                ", fecha=" + fecha +
                ", cliente=" + (cliente != null ? cliente.getUserId() : null) +
                ", items=" + items +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}