package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para representar una boleta en las respuestas
 * Evita problemas de serialización circular y lazy loading
 */
public class BoletaResponseDTO {
    
    private Long boletaId;
    private LocalDateTime fecha;
    private Integer clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private List<DetalleBoletaDTO> items;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String estado;
    private String direccionEntrega;
    private String notas;
    
    // Constructores
    public BoletaResponseDTO() {}
    
    public BoletaResponseDTO(Boleta boleta) {
        this.boletaId = boleta.getBoletaId();
        this.fecha = boleta.getFecha();
        this.total = boleta.getTotal();
        this.estado = boleta.getEstado();
        
        // Información del cliente
        if (boleta.getCliente() != null) {
            this.clienteId = boleta.getCliente().getUserId();
            this.clienteNombre = boleta.getCliente().getNombre();
            this.clienteEmail = boleta.getCliente().getCorreo();
        }
        
        // Convertir items
        if (boleta.getItems() != null && !boleta.getItems().isEmpty()) {
            this.items = boleta.getItems().stream()
                .map(DetalleBoletaDTO::new)
                .collect(Collectors.toList());
            
            // Calcular subtotal sumando los subtotales de los items
            this.subtotal = this.items.stream()
                .map(DetalleBoletaDTO::getSubtotal)
                .filter(s -> s != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Calcular impuestos (IVA 19% de Chile)
            if (this.total != null && this.subtotal != null) {
                this.impuestos = this.total.subtract(this.subtotal);
            }
        } else {
            this.items = new ArrayList<>();
            this.subtotal = BigDecimal.ZERO;
            this.impuestos = BigDecimal.ZERO;
        }
    }
    
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
    
    public Integer getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    public String getClienteEmail() {
        return clienteEmail;
    }
    
    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }
    
    public List<DetalleBoletaDTO> getItems() {
        return items;
    }
    
    public void setItems(List<DetalleBoletaDTO> items) {
        this.items = items;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getImpuestos() {
        return impuestos;
    }
    
    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
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
    
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
}




