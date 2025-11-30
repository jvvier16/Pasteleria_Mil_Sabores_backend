package com.example.Pasteleria_Mil_Sabores.dto;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import java.time.LocalDate;

/**
 * DTO para el perfil del usuario (no expone contraseña)
 */
public class PerfilDTO {
    
    private Integer userId;
    private String nombre;
    private String apellido;
    private String correo;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String role;
    private String imagen;

    public PerfilDTO() {}

    public PerfilDTO(Usuario usuario) {
        this.userId = usuario.getUserId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.correo = usuario.getCorreo();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.direccion = usuario.getDireccion();
        this.telefono = usuario.getTelefono();
        this.role = usuario.getRole();
        this.imagen = usuario.getImagen();
    }

    // Getters y Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}

