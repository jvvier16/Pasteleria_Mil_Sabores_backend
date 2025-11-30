package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.CategoriaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CategoriaControllerV2 - API v2 para categorías (PÚBLICA)
 * 
 * Endpoints:
 * - GET /api/v2/categorias - Listar todas las categorías
 * - GET /api/v2/categorias/{id} - Ver una categoría específica
 */
@RestController
@RequestMapping("/api/v2/categorias")
public class CategoriaControllerV2 {

    private final CategoriaRepository categoriaRepository;

    public CategoriaControllerV2(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> listarCategorias() {
        try {
            List<Categoria> categorias = categoriaRepository.findAll();
            List<CategoriaDTO> categoriasDTO = categorias.stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(categoriasDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener categorías: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaRepository.findById(id).orElse(null);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Categoría no encontrada con ID: " + id));
            }
            return ResponseEntity.ok(ApiResponse.success(new CategoriaDTO(categoria)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener categoría: " + e.getMessage()));
        }
    }
}

