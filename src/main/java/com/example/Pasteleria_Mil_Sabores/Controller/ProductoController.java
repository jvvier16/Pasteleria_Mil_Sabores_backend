package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    // POST: http://localhost:8080/api/v1/productos
    @PostMapping
    public Producto agregarProducto(@RequestBody Producto producto) {
        return service.crearProducto(producto);
    }

    // GET ALL: http://localhost:8080/api/v1/productos
    @GetMapping
    public List<Producto> obtenerTodosLosProductos() {
        return service.obtenerTodosLosProductos();
    }

    // GET BY ID: http://localhost:8080/api/v1/productos/1
    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return service.obtenerProductoPorId(id);
    }

    // PUT: http://localhost:8080/api/v1/productos
    @PutMapping
    public Producto actualizarProducto(@RequestBody Producto producto) {
        return service.actualizarProducto(producto);
    }

    // DELETE: http://localhost:8080/api/v1/productos/1
    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        return service.eliminarProducto(id);
    }
}
