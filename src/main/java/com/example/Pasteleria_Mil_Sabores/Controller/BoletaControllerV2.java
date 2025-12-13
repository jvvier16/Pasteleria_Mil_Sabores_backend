package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import com.example.Pasteleria_Mil_Sabores.Service.BoletaService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.BoletaResponseDTO;
import com.example.Pasteleria_Mil_Sabores.dto.CrearBoletaRequest;
import com.example.Pasteleria_Mil_Sabores.exception.ProductoNoEncontradoException;
import com.example.Pasteleria_Mil_Sabores.exception.StockInsuficienteException;
import com.example.Pasteleria_Mil_Sabores.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BoletaControllerV2 - API v2 para boletas/órdenes
 * 
 * Endpoints públicos y para usuarios autenticados:
 * - GET /api/v2/boletas/mis-pedidos - Usuario ve sus propios pedidos
 * - POST /api/v2/boletas - Usuario crea una orden (compra)
 * - GET /api/v2/boletas/{id} - Obtener boleta por ID
 * - PUT /api/v2/boletas/{id}/cancelar - Cancelar pedido
 */
@RestController
@RequestMapping("/api/v2/boletas")
@Tag(name = "Boletas/Órdenes", description = "API para gestión de pedidos y compras")
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
    @Operation(summary = "Obtener mis pedidos", description = "Obtiene los pedidos del usuario autenticado")
    public ResponseEntity<ApiResponse<List<BoletaResponseDTO>>> obtenerMisPedidos(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Extraer userId del token
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            List<Boleta> misPedidos = boletaService.obtenerBoletasPorUsuario(userId);
            
            // Convertir a DTOs para evitar problemas de serialización
            List<BoletaResponseDTO> pedidosDTO = misPedidos.stream()
                .map(BoletaResponseDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(pedidosDTO));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Error de autenticación: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v2/boletas
     * 
     * Crea una nueva orden/boleta (finalizar compra)
     * 
     * Proceso:
     * 1. Valida los productos y stock
     * 2. Crea la boleta y detalles
     * 3. Calcula totales con IVA
     * 4. Descuenta stock
     * 5. Retorna la boleta creada
     */
    @PostMapping
    @Operation(summary = "Crear orden/boleta", description = "Crea una nueva orden de compra. Puede ser anónimo o autenticado.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Orden creada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> crearBoleta(
            @Valid @RequestBody CrearBoletaRequest request,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            Usuario cliente = null;
            
            // Si hay token, obtener el usuario
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                Integer userId = jwtUtil.extractUserId(token);
                if (userId != null) {
                    cliente = usuarioRepository.findById(userId).orElse(null);
                }
            }
            
            // Procesar la compra
            Boleta boletaCreada = boletaService.procesarCompra(request, cliente);
            
            // Convertir a DTO para la respuesta
            BoletaResponseDTO responseDTO = new BoletaResponseDTO(boletaCreada);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(responseDTO));
            
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(e.getMessage()));
                
        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(e.getMessage()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al procesar la compra: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v2/boletas/{id}
     * 
     * Obtiene una boleta específica (el usuario solo puede ver las suyas)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> obtenerBoletaPorId(
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
            
            // Convertir a DTO
            BoletaResponseDTO responseDTO = new BoletaResponseDTO(boleta);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Error de autenticación"));
        }
    }

    /**
     * PUT /api/v2/boletas/{id}/cancelar
     * 
     * Cancela un pedido del usuario autenticado y restaura el stock
     * Solo se puede cancelar si está en estado "pendiente" (usuarios normales)
     * Admin/Vendedor pueden cancelar en cualquier estado
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<BoletaResponseDTO>> cancelarPedido(
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
            
            // Cancelar y restaurar stock
            Boleta cancelada = boletaService.cancelarBoletaConRestauracionStock(id);
            BoletaResponseDTO responseDTO = new BoletaResponseDTO(cancelada);
            
            return ResponseEntity.ok(ApiResponse.success("Pedido cancelado exitosamente", responseDTO));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al cancelar: " + e.getMessage()));
        }
    }
}
