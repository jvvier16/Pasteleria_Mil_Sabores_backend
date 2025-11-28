package com.example.Pasteleria_Mil_Sabores.dto;

import java.math.BigDecimal;

/**
 * DTO para estadísticas del Dashboard
 */
public class EstadisticasDTO {
    
    private Long totalProductos;
    private Long totalUsuarios;
    private Long totalBoletas;
    private Long totalCategorias;
    private BigDecimal ventasTotales;
    private Long boletasPendientes;
    private Long boletasProcesadas;
    private Long boletasEnviadas;
    private Integer inventarioTotal;
    private Long productosStockCritico;

    public EstadisticasDTO() {}

    // Getters y Setters
    public Long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Long getTotalBoletas() {
        return totalBoletas;
    }

    public void setTotalBoletas(Long totalBoletas) {
        this.totalBoletas = totalBoletas;
    }

    public Long getTotalCategorias() {
        return totalCategorias;
    }

    public void setTotalCategorias(Long totalCategorias) {
        this.totalCategorias = totalCategorias;
    }

    public BigDecimal getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(BigDecimal ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public Long getBoletasPendientes() {
        return boletasPendientes;
    }

    public void setBoletasPendientes(Long boletasPendientes) {
        this.boletasPendientes = boletasPendientes;
    }

    public Long getBoletasProcesadas() {
        return boletasProcesadas;
    }

    public void setBoletasProcesadas(Long boletasProcesadas) {
        this.boletasProcesadas = boletasProcesadas;
    }

    public Long getBoletasEnviadas() {
        return boletasEnviadas;
    }

    public void setBoletasEnviadas(Long boletasEnviadas) {
        this.boletasEnviadas = boletasEnviadas;
    }

    public Integer getInventarioTotal() {
        return inventarioTotal;
    }

    public void setInventarioTotal(Integer inventarioTotal) {
        this.inventarioTotal = inventarioTotal;
    }

    public Long getProductosStockCritico() {
        return productosStockCritico;
    }

    public void setProductosStockCritico(Long productosStockCritico) {
        this.productosStockCritico = productosStockCritico;
    }
}

