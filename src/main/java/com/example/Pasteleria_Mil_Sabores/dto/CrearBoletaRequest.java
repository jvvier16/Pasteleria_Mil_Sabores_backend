package com.example.Pasteleria_Mil_Sabores.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO para crear una nueva boleta/orden de compra
 */
public class CrearBoletaRequest {
    
    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<ItemCarritoDTO> items;
    
    // Información opcional del cliente (para compras sin autenticación)
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String direccionEntrega;
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


