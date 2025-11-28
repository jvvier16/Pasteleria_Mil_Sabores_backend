package com.example.Pasteleria_Mil_Sabores.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    
    private int status;
    private String mensaje;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int status, String mensaje, T data) {
        this.status = status;
        this.mensaje = mensaje;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Métodos estáticos de utilidad
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Éxito", data);
    }

    public static <T> ApiResponse<T> success(String mensaje, T data) {
        return new ApiResponse<>(200, mensaje, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Creado", data);
    }

    public static <T> ApiResponse<T> error(int status, String mensaje) {
        return new ApiResponse<>(status, mensaje, null);
    }

    public static <T> ApiResponse<T> notFound(String mensaje) {
        return new ApiResponse<>(404, mensaje, null);
    }

    public static <T> ApiResponse<T> badRequest(String mensaje) {
        return new ApiResponse<>(400, mensaje, null);
    }

    // Getters y Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

