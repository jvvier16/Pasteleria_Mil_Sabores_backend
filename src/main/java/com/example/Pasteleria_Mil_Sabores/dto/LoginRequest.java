package com.example.Pasteleria_Mil_Sabores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para iniciar sesión")
public class LoginRequest {
    
    @Schema(description = "Correo electrónico del usuario", example = "admin@milsabores.cl")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;
    
    @Schema(description = "Contraseña del usuario", example = "admin123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
