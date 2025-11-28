package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ProductoController - API v1 para productos (PRIVADA)
 * 
 * Según el Excel de requisitos:
 * - API: PRIVADA
 * - Requiere Autenticación: Sí
 * - Roles permitidos: Admin
 * 
 * Endpoints:
 * - GET /api/v1/productos - Lista todos (solo datos básicos)
 * - POST /api/v1/productos - Crea nuevo producto (requiere nombre)
 * - PUT /api/v1/productos/{id} - Actualiza producto (versión 2 con más atributos)
 * - DELETE /api/v1/productos/{id} - Elimina producto (versión 2 permite eliminación)
 * - GET /api/v1/productos/{id} - Detalles de producto (incluye estadísticas avanzadas)
 */
@RestController
@RequestMapping("/api/v1/productos")
@PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'VENDEDOR')")
public class ProductoController {

    @Autowired
    private ProductoService service;

    /**
     * GET /api/v1/productos
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Descripción: Lista todos los productos (solo datos básicos)
     * Respuestas: 200 Éxito, 500 Error servidor
     */
    @GetMapping
    public List<Producto> obtenerTodosLosProductos() {
        return service.obtenerTodosLosProductos();
    }

    /**
     * POST /api/v1/productos
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Datos de entrada: {nombre: "Pikachu"}
     * Descripción: Crea un nuevo producto (requiere nombre de producto)
     * Respuestas: 201 Creado, 400 Datos inválidos
     */
    @PostMapping
    public Producto agregarProducto(@RequestBody Producto producto) {
        return service.crearProducto(producto);
    }

    /**
     * GET /api/v1/productos/{id}
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Descripción: Detalles de un producto (incluye estadísticas avanzadas)
     * Respuestas: 200 Éxito, 404 No encontrado
     */
    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return service.obtenerProductoPorId(id);
    }

    /**
     * PUT /api/v1/productos/{id}
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Datos de entrada: {tipo: "Eléctrico"}
     * Descripción: Actualiza información de un producto (versión 2 con más atributos)
     * Respuestas: 200 Éxito, 404 No encontrado
     */
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setProductoId(id);
        return service.actualizarProducto(producto);
    }

    /**
     * DELETE /api/v1/productos/{id}
     * 
     * API: Privada
     * Requiere Autenticación: Sí
     * Roles permitidos: Admin
     * Descripción: Elimina un producto (versión 2 permite eliminación)
     * Respuestas: 200 Éxito, 404 No encontrado
     */
    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        return service.eliminarProducto(id);
    }
}
