package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Service.CategoriaService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.CategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CategoriaController - API v1 para categorías (ADMIN)
 * 
 * Endpoints:
 * - POST /api/v1/categorias - Crear nueva categoría
 * - GET /api/v1/categorias - Listar todas las categorías
 * - GET /api/v1/categorias/{id} - Obtener categoría por ID
 * - PUT /api/v1/categorias - Actualizar categoría
 * - DELETE /api/v1/categorias/{id} - Eliminar categoría
 */
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * POST /api/v1/categorias
     * Crea una nueva categoría
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<CategoriaDTO>> agregarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            // Validar nombre
            if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El nombre de la categoría es obligatorio"));
            }
            
            // Verificar si ya existe una categoría con el mismo nombre
            List<Categoria> existentes = categoriaRepository.findAll();
            boolean existe = existentes.stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaDTO.getNombre().trim()));
            
            if (existe) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("Ya existe una categoría con ese nombre"));
            }
            
            // Crear la categoría
            Categoria categoria = new Categoria();
            categoria.setNombre(categoriaDTO.getNombre().trim());
            categoria.setDescripcion(categoriaDTO.getDescripcion() != null ? categoriaDTO.getDescripcion().trim() : null);
            
            Categoria guardada = service.crearCategoria(categoria);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(new CategoriaDTO(guardada)));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al crear la categoría: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/categorias
     * Lista todas las categorías
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> obtenerTodasLasCategorias() {
        try {
            List<Categoria> categorias = service.obtenerTodasLasCategorias();
            List<CategoriaDTO> categoriasDTO = categorias.stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(categoriasDTO));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener las categorías: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/categorias/{id}
     * Obtiene una categoría por su ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = service.obtenerCategoriaPorId(id);
            
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Categoría no encontrada con ID: " + id));
            }
            
            return ResponseEntity.ok(ApiResponse.success(new CategoriaDTO(categoria)));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener la categoría: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/v1/categorias
     * Actualiza una categoría existente (el ID viene en el body)
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<CategoriaDTO>> actualizarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            // Validar que se proporcionó el ID
            if (categoriaDTO.getCategoriaId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El ID de la categoría es obligatorio"));
            }
            
            // Validar nombre
            if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("El nombre de la categoría es obligatorio"));
            }
            
            // Verificar que la categoría existe
            Categoria existente = service.obtenerCategoriaPorId(categoriaDTO.getCategoriaId());
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Categoría no encontrada con ID: " + categoriaDTO.getCategoriaId()));
            }
            
            // Verificar que no existe otra categoría con el mismo nombre
            List<Categoria> todasCategorias = categoriaRepository.findAll();
            boolean nombreDuplicado = todasCategorias.stream()
                .anyMatch(c -> !c.getCategoriaId().equals(categoriaDTO.getCategoriaId()) 
                            && c.getNombre().equalsIgnoreCase(categoriaDTO.getNombre().trim()));
            
            if (nombreDuplicado) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("Ya existe otra categoría con ese nombre"));
            }
            
            // Actualizar la categoría
            Categoria categoria = new Categoria();
            categoria.setCategoriaId(categoriaDTO.getCategoriaId());
            categoria.setNombre(categoriaDTO.getNombre().trim());
            categoria.setDescripcion(categoriaDTO.getDescripcion() != null ? categoriaDTO.getDescripcion().trim() : null);
            
            Categoria actualizada = service.actualizarCategoria(categoria);
            
            if (actualizada == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Error al actualizar la categoría"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Categoría actualizada exitosamente", new CategoriaDTO(actualizada)));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al actualizar la categoría: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/v1/categorias/{id}
     * Elimina una categoría por su ID
     * No permite eliminar si hay productos asociados
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<ApiResponse<String>> eliminarCategoria(@PathVariable Long id) {
        try {
            // Verificar que la categoría existe
            Categoria categoria = service.obtenerCategoriaPorId(id);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Categoría no encontrada con ID: " + id));
            }
            
            // Verificar si hay productos usando esta categoría
            long productosAsociados = productoRepository.findByCategoriaCategoriaId(id).size();
            if (productosAsociados > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest(
                        "No se puede eliminar la categoría porque hay " + productosAsociados + 
                        " producto(s) que la utilizan. Reasigne los productos a otra categoría primero."));
            }
            
            // Eliminar la categoría
            service.eliminarCategoria(id);
            
            return ResponseEntity.ok(ApiResponse.success("Categoría eliminada exitosamente", "ID: " + id));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al eliminar la categoría: " + e.getMessage()));
        }
    }
}
