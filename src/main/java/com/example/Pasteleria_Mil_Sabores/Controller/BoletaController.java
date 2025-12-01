package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Service.BoletaService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.BoletaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BoletaController - API v1 para boletas/órdenes (PRIVADA)
 * 
 * API: PRIVADA
 * Requiere Autenticación: Sí
 * Roles permitidos: Admin, Tester, Vendedor
 * 
 * Usa DTOs para evitar problemas de lazy loading y serialización circular
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
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> crearBoleta(@RequestBody Boleta boleta) {
        try {
            Boleta creada = service.crearBoleta(boleta);
            BoletaResponseDTO dto = new BoletaResponseDTO(creada);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(dto));
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
    public ResponseEntity<ApiResponse<List<BoletaResponseDTO>>> obtenerTodasLasBoletas() {
        try {
            List<Boleta> boletas = service.obtenerTodasLasBoletas();
            List<BoletaResponseDTO> dtos = boletas.stream()
                .map(BoletaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener boletas: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/boletas/{id}
     * Obtener una boleta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> obtenerBoletaPorId(@PathVariable Long id) {
        try {
            Boleta boleta = service.obtenerBoletaPorId(id);
            if (boleta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
            }
            BoletaResponseDTO dto = new BoletaResponseDTO(boleta);
            return ResponseEntity.ok(ApiResponse.success(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener boleta: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/boletas/usuario/{userId}
     * Obtener boletas de un usuario específico
     */
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<ApiResponse<List<BoletaResponseDTO>>> obtenerBoletasPorUsuario(@PathVariable Integer userId) {
        try {
            List<Boleta> boletas = service.obtenerBoletasPorUsuario(userId);
            List<BoletaResponseDTO> dtos = boletas.stream()
                .map(BoletaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener boletas del usuario: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/boletas/estado/{estado}
     * Obtener boletas por estado (pendiente, procesado, enviado)
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<BoletaResponseDTO>>> obtenerBoletasPorEstado(@PathVariable String estado) {
        try {
            List<Boleta> boletas = service.obtenerBoletasPorEstado(estado);
            List<BoletaResponseDTO> dtos = boletas.stream()
                .map(BoletaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener boletas por estado: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/v1/boletas/{id}/estado
     * Actualizar el estado de una boleta
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> actualizarEstadoBoleta(
            @PathVariable Long id, 
            @RequestParam String estado) {
        try {
            Boleta actualizada = service.actualizarEstadoBoleta(id, estado);
            if (actualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
            }
            BoletaResponseDTO dto = new BoletaResponseDTO(actualizada);
            return ResponseEntity.ok(ApiResponse.success("Estado actualizado", dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al actualizar estado: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/v1/boletas/{id}
     * Eliminar una boleta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarBoleta(@PathVariable Long id) {
        try {
            Boleta existente = service.obtenerBoletaPorId(id);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Boleta no encontrada con ID: " + id));
            }
            
            String resultado = service.eliminarBoleta(id);
            return ResponseEntity.ok(ApiResponse.success(resultado, "ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al eliminar boleta: " + e.getMessage()));
        }
    }
}
