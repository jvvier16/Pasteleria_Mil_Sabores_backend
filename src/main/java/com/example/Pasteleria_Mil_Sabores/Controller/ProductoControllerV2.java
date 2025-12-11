package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Service.ProductoService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.ProductoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
     * Roles permitidos: Admin
     * Datos de entrada: {nombre, precio, stock, imagen?, descripcion?, categoriaId?}
     * Descripción: Crea un nuevo producto (requiere detalles completos)
     * Respuestas: 201 Creado, 400 Datos inválidos
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@RequestBody Map<String, Object> productoData) {
        try {
            // Validaciones
            String nombre = (String) productoData.get("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El nombre del producto es obligatorio"));
            }
            
            Object precioObj = productoData.get("precio");
            if (precioObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El precio del producto es obligatorio"));
            }

            // Crear el producto
            Producto producto = new Producto();
            producto.setNombre(nombre.trim());
            
            // Manejar precio (puede venir como String, Integer o Double)
            BigDecimal precio;
            if (precioObj instanceof Number) {
                precio = new BigDecimal(precioObj.toString());
            } else if (precioObj instanceof String) {
                precio = new BigDecimal((String) precioObj);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El precio no tiene un formato válido"));
            }
            producto.setPrecio(precio);
            
            // Stock
            Object stockObj = productoData.get("stock");
            if (stockObj != null) {
                if (stockObj instanceof Number) {
                    producto.setStock(((Number) stockObj).intValue());
                } else if (stockObj instanceof String) {
                    producto.setStock(Integer.parseInt((String) stockObj));
                }
            }
            
            // Imagen
            String imagen = (String) productoData.get("imagen");
            if (imagen != null) {
                producto.setImagen(imagen);
            }
            
            // Descripción
            String descripcion = (String) productoData.get("descripcion");
            if (descripcion != null) {
                producto.setDescripcion(descripcion);
            }
            
            // Manejar categoría por ID
            Object categoriaIdObj = productoData.get("categoriaId");
            if (categoriaIdObj != null) {
                Long categoriaId;
                if (categoriaIdObj instanceof Number) {
                    categoriaId = ((Number) categoriaIdObj).longValue();
                } else if (categoriaIdObj instanceof String) {
                    categoriaId = Long.parseLong((String) categoriaIdObj);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("El ID de categoría no tiene un formato válido"));
                }
                
                Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
                if (categoria == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Categoría no encontrada con ID: " + categoriaId));
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
     * Roles permitidos: Admin
     * Datos de entrada: {nombre?, precio?, stock?, imagen?, descripcion?, categoriaId?}
     * Descripción: Actualiza información de un producto
     * Respuestas: 200 Éxito, 400 Datos inválidos, 404 No encontrado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> productoData) {
        
        Producto existente = productoService.obtenerProductoPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Producto no encontrado con ID: " + id));
        }

        try {
            // Actualizar nombre
            String nombre = (String) productoData.get("nombre");
            if (nombre != null) {
                if (nombre.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("El nombre no puede estar vacío"));
                }
                existente.setNombre(nombre.trim());
            }
            
            // Actualizar precio
            Object precioObj = productoData.get("precio");
            if (precioObj != null) {
                BigDecimal precio;
                if (precioObj instanceof Number) {
                    precio = new BigDecimal(precioObj.toString());
                } else if (precioObj instanceof String) {
                    precio = new BigDecimal((String) precioObj);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("El precio no tiene un formato válido"));
                }
                existente.setPrecio(precio);
            }
            
            // Actualizar stock
            Object stockObj = productoData.get("stock");
            if (stockObj != null) {
                if (stockObj instanceof Number) {
                    existente.setStock(((Number) stockObj).intValue());
                } else if (stockObj instanceof String) {
                    existente.setStock(Integer.parseInt((String) stockObj));
                }
            }
            
            // Actualizar imagen
            if (productoData.containsKey("imagen")) {
                String imagen = (String) productoData.get("imagen");
                existente.setImagen(imagen);
            }
            
            // Actualizar descripción
            if (productoData.containsKey("descripcion")) {
                String descripcion = (String) productoData.get("descripcion");
                existente.setDescripcion(descripcion);
            }
            
            // Actualizar categoría por ID
            if (productoData.containsKey("categoriaId")) {
                Object categoriaIdObj = productoData.get("categoriaId");
                if (categoriaIdObj == null) {
                    // Si se envía null, quitar la categoría
                    existente.setCategoria(null);
                } else {
                    Long categoriaId;
                    if (categoriaIdObj instanceof Number) {
                        categoriaId = ((Number) categoriaIdObj).longValue();
                    } else if (categoriaIdObj instanceof String) {
                        categoriaId = Long.parseLong((String) categoriaIdObj);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.badRequest("El ID de categoría no tiene un formato válido"));
                    }
                    
                    Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
                    if (categoria == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.badRequest("Categoría no encontrada con ID: " + categoriaId));
                    }
                    existente.setCategoria(categoria);
                }
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
