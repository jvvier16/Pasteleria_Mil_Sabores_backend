package com.example.Pasteleria_Mil_Sabores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO para crear una nueva boleta/orden de compra
 */
@Schema(description = "Datos para crear una nueva orden de compra")
public class CrearBoletaRequest {
    
    @Schema(description = "Lista de productos a comprar", example = "[{\"productoId\": 1, \"cantidad\": 2}]")
    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<ItemCarritoDTO> items;
    
    @Schema(description = "Nombre del cliente (opcional si está autenticado)", example = "Juan Pérez")
    private String nombreCliente;
    
    @Schema(description = "Email del cliente (opcional si está autenticado)", example = "juan@ejemplo.com")
    private String emailCliente;
    
    @Schema(description = "Teléfono del cliente", example = "+56912345678")
    private String telefonoCliente;
    
    @Schema(description = "Dirección de entrega", example = "Av. Principal 123, Santiago")
    private String direccionEntrega;
    
    @Schema(description = "Notas adicionales para el pedido", example = "Entregar después de las 18:00")
    private String notas;
    
    // Constructores
    public CrearBoletaRequest() {}
    
    public CrearBoletaRequest(List<ItemCarritoDTO> items) {
        this.items = items;
    }
    
    // Getters y Setters
    public List<ItemCarritoDTO> getItems() {
        return items;
    }
    
    public void setItems(List<ItemCarritoDTO> items) {
        this.items = items;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public String getEmailCliente() {
        return emailCliente;
    }
    
    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
    
    public String getTelefonoCliente() {
        return telefonoCliente;
    }
    
    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
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
