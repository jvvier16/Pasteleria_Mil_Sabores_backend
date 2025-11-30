package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import com.example.Pasteleria_Mil_Sabores.Service.BoletaService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoletaControllerV2 - API v2 para boletas/órdenes
 * 
 * Endpoints públicos y para usuarios autenticados:
 * - GET /api/v2/boletas/mis-pedidos - Usuario ve sus propios pedidos
 * - POST /api/v2/boletas - Usuario crea una orden (compra)
 */
@RestController
@RequestMapping("/api/v2/boletas")
public class BoletaControllerV2 {

    private final BoletaService boletaService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public BoletaControllerV2(BoletaService boletaService, UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.boletaService = boletaService;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * GET /api/v2/boletas/mis-pedidos
     * 
     * Obtiene los pedidos del usuario autenticado
     * Requiere autenticación (cualquier rol)
     */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<ApiResponse<List<Boleta>>> obtenerMisPedidos(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Extraer userId del token
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            List<Boleta> misPedidos = boletaService.obtenerBoletasPorUsuario(userId);
            return ResponseEntity.ok(ApiResponse.success(misPedidos));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Error de autenticación: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v2/boletas
     * 
     * Crea una nueva orden/boleta (finalizar compra)
     * Requiere autenticación (cualquier rol)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Boleta>> crearBoleta(
            @RequestBody Boleta boleta,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Si hay token, asociar la boleta al usuario
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                Integer userId = jwtUtil.extractUserId(token);
                if (userId != null) {
                    Usuario cliente = usuarioRepository.findById(userId).orElse(null);
                    if (cliente != null) {
                        boleta.setCliente(cliente);
                    }
                }
            }
            
            Boleta creada = boletaService.crearBoleta(boleta);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(creada));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error al crear boleta: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v2/boletas/{id}
     * 
     * Obtiene una boleta específica (el usuario solo puede ver las suyas)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Boleta>> obtenerBoletaPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            
            Boleta boleta = boletaService.obtenerBoletaPorId(id);
            
            if (boleta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Boleta no encontrada"));
            }
            
            // Admin, Tester y Vendedor pueden ver cualquier boleta
            // Users solo pueden ver sus propias boletas
            String roleLower = role != null ? role.toLowerCase() : "";
            boolean esPrivilegiado = roleLower.equals("admin") || 
                                     roleLower.equals("tester") || 
                                     roleLower.equals("vendedor");
            
            if (!esPrivilegiado) {
                // Verificar que la boleta pertenece al usuario
                if (boleta.getCliente() == null || 
                    !boleta.getCliente().getUserId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(403, "No tienes permiso para ver esta boleta"));
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success(boleta));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Error de autenticación"));
        }
    }

    /**
     * PUT /api/v2/boletas/{id}/cancelar
     * 
     * Cancela un pedido del usuario autenticado
     * Solo se puede cancelar si está en estado "pendiente"
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<Boleta>> cancelarPedido(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            Boleta boleta = boletaService.obtenerBoletaPorId(id);
            if (boleta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Pedido no encontrado"));
            }
            
            // Verificar permisos
            String roleLower = role != null ? role.toLowerCase() : "";
            boolean esPrivilegiado = roleLower.equals("admin") || 
                                     roleLower.equals("tester") || 
                                     roleLower.equals("vendedor");
            
            // Solo el dueño o un privilegiado puede cancelar
            if (!esPrivilegiado && (boleta.getCliente() == null || 
                !boleta.getCliente().getUserId().equals(userId))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "No tienes permiso para cancelar este pedido"));
            }
            
            // Solo se puede cancelar si está pendiente (usuarios normales)
            String estadoActual = boleta.getEstado() != null ? boleta.getEstado().toLowerCase() : "";
            if (!estadoActual.equals("pendiente") && !esPrivilegiado) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("Solo se pueden cancelar pedidos pendientes. Estado actual: " + boleta.getEstado()));
            }
            
            Boleta cancelada = boletaService.actualizarEstadoBoleta(id, "cancelado");
            return ResponseEntity.ok(ApiResponse.success("Pedido cancelado exitosamente", cancelada));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al cancelar: " + e.getMessage()));
        }
    }
}

