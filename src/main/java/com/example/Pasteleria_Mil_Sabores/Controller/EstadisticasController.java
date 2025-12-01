package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Repository.BoletaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.EstadisticasDTO;
import com.example.Pasteleria_Mil_Sabores.dto.ReporteVentasDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EstadisticasController - Endpoints para dashboard y reportes
 * 
 * Endpoints:
 * - GET /api/v1/estadisticas - Estadísticas generales del dashboard
 * - GET /api/v1/reportes/ventas - Reporte de ventas
 */
@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
public class EstadisticasController {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BoletaRepository boletaRepository;
    private final CategoriaRepository categoriaRepository;

    public EstadisticasController(
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            BoletaRepository boletaRepository,
            CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.boletaRepository = boletaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * GET /api/v1/estadisticas
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin, Tester
     * Descripción: Obtiene estadísticas generales para el dashboard
     * Respuestas: 200 Éxito, 500 Error servidor
     */
    @GetMapping("/estadisticas")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<EstadisticasDTO>> obtenerEstadisticas() {
        try {
            EstadisticasDTO stats = new EstadisticasDTO();
            
            // Totales básicos
            stats.setTotalProductos(productoRepository.count());
            stats.setTotalUsuarios(usuarioRepository.count());
            stats.setTotalBoletas(boletaRepository.count());
            stats.setTotalCategorias(categoriaRepository.count());
            
            // Calcular ventas totales
            List<Boleta> boletas = boletaRepository.findAll();
            BigDecimal ventasTotales = boletas.stream()
                .map(b -> b.getTotal() != null ? b.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setVentasTotales(ventasTotales);
            
            // Boletas por estado
            stats.setBoletasPendientes(
                boletas.stream().filter(b -> "pendiente".equalsIgnoreCase(b.getEstado())).count()
            );
            stats.setBoletasProcesadas(
                boletas.stream().filter(b -> "procesado".equalsIgnoreCase(b.getEstado())).count()
            );
            stats.setBoletasEnviadas(
                boletas.stream().filter(b -> "enviado".equalsIgnoreCase(b.getEstado())).count()
            );
            
            // Inventario
            List<Producto> productos = productoRepository.findAll();
            int inventarioTotal = productos.stream()
                .mapToInt(p -> p.getStock() != null ? p.getStock() : 0)
                .sum();
            stats.setInventarioTotal(inventarioTotal);
            
            // Productos en stock crítico (menos de 5 unidades)
            long stockCritico = productos.stream()
                .filter(p -> p.getStock() != null && p.getStock() < 5)
                .count();
            stats.setProductosStockCritico(stockCritico);
            
            return ResponseEntity.ok(ApiResponse.success(stats));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/reportes/ventas
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin, Tester
     * Descripción: Obtiene reporte de ventas con filtros opcionales de fecha
     * Parámetros: fechaInicio (opcional), fechaFin (opcional)
     * Respuestas: 200 Éxito, 500 Error servidor
     */
    @GetMapping("/reportes/ventas")
    @Transactional(readOnly = true) // Necesario para acceder a colecciones lazy (boleta.items)
    public ResponseEntity<ApiResponse<ReporteVentasDTO>> obtenerReporteVentas(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {
        try {
            ReporteVentasDTO reporte = new ReporteVentasDTO();
            
            // Establecer fechas por defecto (últimos 30 días)
            LocalDate inicio = fechaInicio != null ? fechaInicio : LocalDate.now().minusDays(30);
            LocalDate fin = fechaFin != null ? fechaFin : LocalDate.now();
            
            reporte.setFechaInicio(inicio);
            reporte.setFechaFin(fin);
            
            // Obtener todas las boletas
            List<Boleta> todasBoletas = boletaRepository.findAll();
            
            // Filtrar por fecha (si la boleta tiene fecha)
            List<Boleta> boletasFiltradas = todasBoletas.stream()
                .filter(b -> {
                    if (b.getFecha() == null) return true; // Incluir si no tiene fecha
                    LocalDate fechaBoleta = b.getFecha().toLocalDate();
                    return !fechaBoleta.isBefore(inicio) && !fechaBoleta.isAfter(fin);
                })
                .collect(Collectors.toList());
            
            // Calcular totales
            reporte.setTotalOrdenes((long) boletasFiltradas.size());
            
            BigDecimal ventasTotales = boletasFiltradas.stream()
                .map(b -> b.getTotal() != null ? b.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            reporte.setVentasTotales(ventasTotales);
            
            // Promedio de venta
            if (!boletasFiltradas.isEmpty()) {
                BigDecimal promedio = ventasTotales.divide(
                    BigDecimal.valueOf(boletasFiltradas.size()), 
                    2, 
                    RoundingMode.HALF_UP
                );
                reporte.setPromedioVenta(promedio);
            } else {
                reporte.setPromedioVenta(BigDecimal.ZERO);
            }
            
            // Ventas por día (agrupadas)
            Map<LocalDate, List<Boleta>> boletasPorDia = boletasFiltradas.stream()
                .filter(b -> b.getFecha() != null)
                .collect(Collectors.groupingBy(b -> b.getFecha().toLocalDate()));
            
            List<ReporteVentasDTO.VentaPorDia> ventasPorDia = boletasPorDia.entrySet().stream()
                .map(entry -> {
                    BigDecimal totalDia = entry.getValue().stream()
                        .map(b -> b.getTotal() != null ? b.getTotal() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new ReporteVentasDTO.VentaPorDia(
                        entry.getKey(),
                        (long) entry.getValue().size(),
                        totalDia
                    );
                })
                .sorted(Comparator.comparing(ReporteVentasDTO.VentaPorDia::getFecha))
                .collect(Collectors.toList());
            reporte.setVentasPorDia(ventasPorDia);
            
            // Productos más vendidos (top 5)
            Map<Long, Integer> ventasPorProducto = new HashMap<>();
            Map<Long, String> nombresProductos = new HashMap<>();
            Map<Long, BigDecimal> preciosProductos = new HashMap<>();
            
            for (Boleta boleta : boletasFiltradas) {
                if (boleta.getItems() != null) {
                    boleta.getItems().forEach(detalle -> {
                        if (detalle.getProducto() != null) {
                            Long prodId = detalle.getProducto().getProductoId();
                            int cantidad = detalle.getCantidad() != null ? detalle.getCantidad() : 0;
                            ventasPorProducto.merge(prodId, cantidad, Integer::sum);
                            nombresProductos.putIfAbsent(prodId, detalle.getProducto().getNombre());
                            preciosProductos.putIfAbsent(prodId, detalle.getProducto().getPrecio());
                        }
                    });
                }
            }
            
            List<ReporteVentasDTO.ProductoMasVendido> productosMasVendidos = ventasPorProducto.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    BigDecimal precio = preciosProductos.getOrDefault(entry.getKey(), BigDecimal.ZERO);
                    BigDecimal totalVentas = precio.multiply(BigDecimal.valueOf(entry.getValue()));
                    return new ReporteVentasDTO.ProductoMasVendido(
                        entry.getKey(),
                        nombresProductos.get(entry.getKey()),
                        entry.getValue(),
                        totalVentas
                    );
                })
                .collect(Collectors.toList());
            reporte.setProductosMasVendidos(productosMasVendidos);
            
            return ResponseEntity.ok(ApiResponse.success(reporte));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "Error al generar reporte: " + e.getMessage()));
        }
    }
}

