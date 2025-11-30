package com.example.Pasteleria_Mil_Sabores.dto;

/**
 * DTO para solicitud de cambio de contraseña
 */
public class CambioPasswordRequest {
    
    private String contrasenaActual;
    private String contrasenaNueva;

    public CambioPasswordRequest() {}

    // Getters y Setters
    public String getContrasenaActual() { return contrasenaActual; }
    public void setContrasenaActual(String contrasenaActual) { this.contrasenaActual = contrasenaActual; }
    public String getContrasenaNueva() { return contrasenaNueva; }
    public void setContrasenaNueva(String contrasenaNueva) { this.contrasenaNueva = contrasenaNueva; }
}

