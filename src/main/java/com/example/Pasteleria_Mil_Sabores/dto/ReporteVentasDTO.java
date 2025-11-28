package com.example.Pasteleria_Mil_Sabores.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para reportes de ventas
 */
public class ReporteVentasDTO {
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long totalOrdenes;
    private BigDecimal ventasTotales;
    private BigDecimal promedioVenta;
    private List<VentaPorDia> ventasPorDia;
    private List<ProductoMasVendido> productosMasVendidos;

    public ReporteVentasDTO() {}

    // Getters y Setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getTotalOrdenes() {
        return totalOrdenes;
    }

    public void setTotalOrdenes(Long totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }

    public BigDecimal getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(BigDecimal ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public BigDecimal getPromedioVenta() {
        return promedioVenta;
    }

    public void setPromedioVenta(BigDecimal promedioVenta) {
        this.promedioVenta = promedioVenta;
    }

    public List<VentaPorDia> getVentasPorDia() {
        return ventasPorDia;
    }

    public void setVentasPorDia(List<VentaPorDia> ventasPorDia) {
        this.ventasPorDia = ventasPorDia;
    }

    public List<ProductoMasVendido> getProductosMasVendidos() {
        return productosMasVendidos;
    }

    public void setProductosMasVendidos(List<ProductoMasVendido> productosMasVendidos) {
        this.productosMasVendidos = productosMasVendidos;
    }

    // Clases internas para estructurar datos
    public static class VentaPorDia {
        private LocalDate fecha;
        private Long cantidadOrdenes;
        private BigDecimal totalVentas;

        public VentaPorDia() {}

        public VentaPorDia(LocalDate fecha, Long cantidadOrdenes, BigDecimal totalVentas) {
            this.fecha = fecha;
            this.cantidadOrdenes = cantidadOrdenes;
            this.totalVentas = totalVentas;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public Long getCantidadOrdenes() {
            return cantidadOrdenes;
        }

        public void setCantidadOrdenes(Long cantidadOrdenes) {
            this.cantidadOrdenes = cantidadOrdenes;
        }

        public BigDecimal getTotalVentas() {
            return totalVentas;
        }

        public void setTotalVentas(BigDecimal totalVentas) {
            this.totalVentas = totalVentas;
        }
    }

    public static class ProductoMasVendido {
        private Long productoId;
        private String nombre;
        private Integer cantidadVendida;
        private BigDecimal totalVentas;

        public ProductoMasVendido() {}

        public ProductoMasVendido(Long productoId, String nombre, Integer cantidadVendida, BigDecimal totalVentas) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.cantidadVendida = cantidadVendida;
            this.totalVentas = totalVentas;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getCantidadVendida() {
            return cantidadVendida;
        }

        public void setCantidadVendida(Integer cantidadVendida) {
            this.cantidadVendida = cantidadVendida;
        }

        public BigDecimal getTotalVentas() {
            return totalVentas;
        }

        public void setTotalVentas(BigDecimal totalVentas) {
            this.totalVentas = totalVentas;
        }
    }
}

