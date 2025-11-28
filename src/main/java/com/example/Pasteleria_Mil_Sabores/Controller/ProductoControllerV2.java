package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Service.ProductoService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.ProductoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductoControllerV2 - API v2 para productos
 * 
 * Según el Excel de requisitos:
 * - GET /api/v2/productos - Público, incluye estadísticas detalladas
 * - GET /api/v2/productos/{id} - Público, detalles incluyen habilidades
 * - POST /api/v2/productos - Requiere Admin, requiere detalles completos
 * - PUT /api/v2/productos/{id} - Requiere Admin, permite actualización de nombre y tipo
 * - DELETE /api/v2/productos/{id} - Requiere Admin, eliminación segura
 */
@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProductoControllerV2(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    /**
     * GET /api/v2/productos
     * 
     * API: Pública
     * Requiere Autenticación: No
     * Descripción: Lista todos los productos con estadísticas detalladas
     * Respuestas: 200 Éxito, 500 Error servidor
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> listarTodosLosProductos() {
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            List<ProductoDTO> productosDTO = productos.stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(productosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error del servidor: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v2/productos/{id}
     * 
     * API: Pública
     * Requiere Autenticación: No
     * Descripción: Obtiene detalles de un producto (incluye habilidades/detalles)
     * Respuestas: 200 Éxito, 404 No encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
        }
        
        return ResponseEntity.ok(ApiResponse.success(new ProductoDTO(producto)));
    }

    /**
     * POST /api/v2/productos
     * 
     * API: Pública (pero requiere autenticación)
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Datos de entrada: {nombre: "Pikachu", tipo: ...}
     * Descripción: Crea un nuevo producto (requiere detalles completos)
     * Respuestas: 201 Creado, 400 Datos inválidos
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@RequestBody Producto producto) {
        // Validaciones
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("El nombre del producto es obligatorio"));
        }
        
        if (producto.getPrecio() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("El precio del producto es obligatorio"));
        }

        try {
            Producto guardado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(new ProductoDTO(guardado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error al crear producto: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/v2/productos/{id}
     * 
     * API: Pública (pero requiere autenticación)
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Datos de entrada: {nombre: "Raichu", tipo: "Eléctrico"...}
     * Descripción: Actualiza información de un producto (nombre y tipo)
     * Respuestas: 200 Éxito, 400 Datos inválidos, 404 No encontrado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @PathVariable Long id, 
            @RequestBody Producto productoActualizado) {
        
        Producto existente = productoService.obtenerProductoPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
        }

        // Validar datos de entrada
        if (productoActualizado.getNombre() != null && productoActualizado.getNombre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("El nombre no puede estar vacío"));
        }

        // Actualizar campos
        if (productoActualizado.getNombre() != null) {
            existente.setNombre(productoActualizado.getNombre());
        }
        if (productoActualizado.getPrecio() != null) {
            existente.setPrecio(productoActualizado.getPrecio());
        }
        if (productoActualizado.getStock() != null) {
            existente.setStock(productoActualizado.getStock());
        }
        if (productoActualizado.getImagen() != null) {
            existente.setImagen(productoActualizado.getImagen());
        }
        if (productoActualizado.getDescripcion() != null) {
            existente.setDescripcion(productoActualizado.getDescripcion());
        }
        if (productoActualizado.getCategoria() != null) {
            existente.setCategoria(productoActualizado.getCategoria());
        }

        try {
            Producto actualizado = productoService.actualizarProducto(existente);
            return ResponseEntity.ok(ApiResponse.success("Producto actualizado", new ProductoDTO(actualizado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error al actualizar: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/v2/productos/{id}
     * 
     * API: Pública (pero requiere autenticación)
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Descripción: Elimina un producto (eliminación segura)
     * Respuestas: 200 Éxito, 404 No encontrado
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<String>> eliminarProducto(@PathVariable Long id) {
        Producto existente = productoService.obtenerProductoPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
        }

        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", "ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al eliminar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v2/productos/buscar?q=
     * 
     * API: Pública
     * Requiere Autenticación: No
     * Descripción: Busca productos por nombre o descripción
     * Parámetros: q (query de búsqueda)
     * Respuestas: 200 Éxito, 400 Parámetro faltante
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> buscarProductos(
            @RequestParam(required = false) String q) {
        
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("El parámetro de búsqueda 'q' es requerido"));
        }

        try {
            List<Producto> productos = productoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(q.trim(), q.trim());
            
            List<ProductoDTO> productosDTO = productos.stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                "Se encontraron " + productosDTO.size() + " productos", 
                productosDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error en la búsqueda: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v2/productos/categoria/{categoriaId}
     * 
     * API: Pública
     * Requiere Autenticación: No
     * Descripción: Obtiene productos de una categoría específica
     * Respuestas: 200 Éxito, 404 Categoría no encontrada
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerProductosPorCategoria(
            @PathVariable Long categoriaId) {
        
        try {
            List<Producto> productos = productoRepository.findByCategoriaCategoriaId(categoriaId);
            
            List<ProductoDTO> productosDTO = productos.stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(productosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener productos: " + e.getMessage()));
        }
    }
}

