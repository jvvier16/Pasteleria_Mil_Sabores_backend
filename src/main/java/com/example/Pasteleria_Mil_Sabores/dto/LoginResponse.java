package com.example.Pasteleria_Mil_Sabores.dto;

public class LoginResponse {
    
    private String token;
    private String tipo = "Bearer";
    private Integer userId;
    private String nombre;
    private String correo;
    private String role;
    private String mensaje;

    public LoginResponse() {}

    public LoginResponse(String token, Integer userId, String nombre, String correo, String role) {
        this.token = token;
        this.userId = userId;
        this.nombre = nombre;
        this.correo = correo;
        this.role = role;
        this.mensaje = "Login exitoso";
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

