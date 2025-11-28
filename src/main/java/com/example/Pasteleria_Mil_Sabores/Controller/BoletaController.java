package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Service.BoletaService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * BoletaController - API v1 para boletas/órdenes (PRIVADA)
 * 
 * API: PRIVADA
 * Requiere Autenticación: Sí
 * Roles permitidos: Admin
 */
@RestController
@RequestMapping("/api/v1/boletas")
@PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
public class BoletaController {

    @Autowired
    private BoletaService service;

    /**
     * POST /api/v1/boletas
     * Crear una nueva boleta (finalizar compra)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Boleta>> crearBoleta(@RequestBody Boleta boleta) {
        try {
            Boleta creada = service.crearBoleta(boleta);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(creada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error al crear boleta: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/boletas
     * Obtener todas las boletas (Admin)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Boleta>>> obtenerTodasLasBoletas() {
        List<Boleta> boletas = service.obtenerTodasLasBoletas();
        return ResponseEntity.ok(ApiResponse.success(boletas));
    }

    /**
     * GET /api/v1/boletas/{id}
     * Obtener una boleta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Boleta>> obtenerBoletaPorId(@PathVariable Long id) {
        Boleta boleta = service.obtenerBoletaPorId(id);
        if (boleta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success(boleta));
    }

    /**
     * GET /api/v1/boletas/usuario/{userId}
     * Obtener boletas de un usuario específico
     */
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ApiResponse<List<Boleta>>> obtenerBoletasPorUsuario(@PathVariable Integer userId) {
        List<Boleta> boletas = service.obtenerBoletasPorUsuario(userId);
        return ResponseEntity.ok(ApiResponse.success(boletas));
    }

    /**
     * GET /api/v1/boletas/estado/{estado}
     * Obtener boletas por estado (pendiente, procesado, enviado)
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<Boleta>>> obtenerBoletasPorEstado(@PathVariable String estado) {
        List<Boleta> boletas = service.obtenerBoletasPorEstado(estado);
        return ResponseEntity.ok(ApiResponse.success(boletas));
    }

    /**
     * PUT /api/v1/boletas/{id}/estado
     * Actualizar el estado de una boleta
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Boleta>> actualizarEstadoBoleta(
            @PathVariable Long id, 
            @RequestParam String estado) {
        
        Boleta actualizada = service.actualizarEstadoBoleta(id, estado);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado", actualizada));
    }

    /**
     * DELETE /api/v1/boletas/{id}
     * Eliminar una boleta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarBoleta(@PathVariable Long id) {
        Boleta existente = service.obtenerBoletaPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
        }
        
        String resultado = service.eliminarBoleta(id);
        return ResponseEntity.ok(ApiResponse.success(resultado, "ID: " + id));
    }
}
