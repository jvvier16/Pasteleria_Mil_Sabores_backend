package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias") // Ruta base para todos los endpoints de Categoria
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    // POST: http://localhost:8080/api/v1/categorias
    @PostMapping
    public Categoria agregarCategoria(@RequestBody Categoria categoria) {
        return service.crearCategoria(categoria);
    }

    // GET ALL: http://localhost:8080/api/v1/categorias
    @GetMapping
    public List<Categoria> obtenerTodasLasCategorias() {
        return service.obtenerTodasLasCategorias();
    }

    // GET BY ID: http://localhost:8080/api/v1/categorias/1
    @GetMapping("/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return service.obtenerCategoriaPorId(id);
    }

    // PUT: http://localhost:8080/api/v1/categorias
    // Nota: El ID se espera dentro del cuerpo (RequestBody) de la Categoría
    @PutMapping
    public Categoria actualizarCategoria(@RequestBody Categoria categoria) {
        return service.actualizarCategoria(categoria);
    }

    // DELETE: http://localhost:8080/api/v1/categorias/1
    @DeleteMapping("/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        return service.eliminarCategoria(id);
    }
}
