package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Service.ProductoService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.ProductoDTO;
import com.example.Pasteleria_Mil_Sabores.dto.ProductoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoControllerV2(ProductoService productoService, 
                                 ProductoRepository productoRepository,
                                 CategoriaRepository categoriaRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
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
        try {
            // Validar que el ID sea válido
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("ID de producto inválido: " + id));
            }
            
            Producto producto = productoService.obtenerProductoPorId(id);
            
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
            }
            
            // Validar que el producto tenga datos válidos
            ProductoDTO productoDTO = new ProductoDTO(producto);
            if (productoDTO.getProductoId() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Error al procesar el producto"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(productoDTO));
        } catch (Exception e) {
            // Manejar cualquier excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener producto: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v2/productos
     * 
     * API: Pública (pero requiere autenticación)
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin, Tester, Vendedor
     * Datos de entrada: {nombre, precio, stock, imagen?, descripcion?, categoriaId?}
     * Descripción: Crea un nuevo producto (requiere detalles completos)
     * Respuestas: 201 Creado, 400 Datos inválidos
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Crea un nuevo producto en la base de datos. Requiere autenticación con rol ADMIN, TESTER o VENDEDOR."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del producto a crear",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductoRequestDTO.class))
            )
            @RequestBody ProductoRequestDTO productoData) {
        try {
            // Validaciones
            if (productoData.getNombre() == null || productoData.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El nombre del producto es obligatorio"));
            }
            
            if (productoData.getPrecio() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El precio del producto es obligatorio"));
            }

            // Crear el producto
            Producto producto = new Producto();
            producto.setNombre(productoData.getNombre().trim());
            producto.setPrecio(productoData.getPrecio());
            
            // Stock
            if (productoData.getStock() != null) {
                producto.setStock(productoData.getStock());
            }
            
            // Imagen
            if (productoData.getImagen() != null) {
                producto.setImagen(productoData.getImagen());
            }
            
            // Descripción
            if (productoData.getDescripcion() != null) {
                producto.setDescripcion(productoData.getDescripcion());
            }
            
            // Manejar categoría por ID
            if (productoData.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(productoData.getCategoriaId()).orElse(null);
                if (categoria == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Categoría no encontrada con ID: " + productoData.getCategoriaId()));
                }
                producto.setCategoria(categoria);
            }

            Producto guardado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(new ProductoDTO(guardado)));
                
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error en el formato de los números: " + e.getMessage()));
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
     * Roles permitidos: Admin, Tester, Vendedor
     * Datos de entrada: {nombre?, precio?, stock?, imagen?, descripcion?, categoriaId?}
     * Descripción: Actualiza información de un producto
     * Respuestas: 200 Éxito, 400 Datos inválidos, 404 No encontrado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    @Operation(
        summary = "Actualizar un producto",
        description = "Actualiza los datos de un producto existente. Todos los campos son opcionales."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar") @PathVariable Long id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del producto a actualizar (todos los campos son opcionales)",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductoRequestDTO.class))
            )
            @RequestBody ProductoRequestDTO productoData) {
        
        Producto existente = productoService.obtenerProductoPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
        }

        try {
            // Actualizar nombre
            if (productoData.getNombre() != null) {
                if (productoData.getNombre().trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("El nombre no puede estar vacío"));
                }
                existente.setNombre(productoData.getNombre().trim());
            }
            
            // Actualizar precio
            if (productoData.getPrecio() != null) {
                existente.setPrecio(productoData.getPrecio());
            }
            
            // Actualizar stock
            if (productoData.getStock() != null) {
                existente.setStock(productoData.getStock());
            }
            
            // Actualizar imagen
            if (productoData.getImagen() != null) {
                existente.setImagen(productoData.getImagen());
            }
            
            // Actualizar descripción
            if (productoData.getDescripcion() != null) {
                existente.setDescripcion(productoData.getDescripcion());
            }
            
            // Actualizar categoría por ID
            if (productoData.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(productoData.getCategoriaId()).orElse(null);
                if (categoria == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Categoría no encontrada con ID: " + productoData.getCategoriaId()));
                }
                existente.setCategoria(categoria);
            }

            Producto actualizado = productoService.actualizarProducto(existente);
            return ResponseEntity.ok(ApiResponse.success("Producto actualizado", new ProductoDTO(actualizado)));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error en el formato de los números: " + e.getMessage()));
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
