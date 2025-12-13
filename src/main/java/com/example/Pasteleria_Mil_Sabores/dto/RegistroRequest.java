package com.example.Pasteleria_Mil_Sabores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para el registro de nuevos usuarios
 */
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegistroRequest {
    
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;
    
    @Schema(description = "Correo electrónico (será el nombre de usuario)", example = "juan@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;
    
    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "miPassword123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    @Schema(description = "Teléfono del usuario", example = "+56912345678")
    private String telefono;
    
    @Schema(description = "Dirección del usuario", example = "Av. Principal 123, Santiago")
    private String direccion;

    // Constructores
    public RegistroRequest() {}

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}


