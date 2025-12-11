package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * POST: Crear/Guardar una categoría
     */
    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * GET ALL: Obtener todas las categorías
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * GET BY ID: Obtener una categoría por su ID
     */
    @Transactional(readOnly = true)
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }
    
    /**
     * Busca una categoría por ID (devuelve Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * PUT: Actualizar una categoría existente
     */
    @Transactional
    public Categoria actualizarCategoria(Categoria categoriaActualizada) {
        Categoria existingCategoria = categoriaRepository.findById(categoriaActualizada.getCategoriaId()).orElse(null);
        if (existingCategoria != null) {
            existingCategoria.setNombre(categoriaActualizada.getNombre());
            existingCategoria.setDescripcion(categoriaActualizada.getDescripcion());
            return categoriaRepository.save(existingCategoria);
        }
        return null;
    }

    /**
     * DELETE: Eliminar una categoría por su ID
     */
    @Transactional
    public String eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
        return "Categoría con ID " + id + " eliminada.";
    }
    
    /**
     * Verifica si una categoría tiene productos asociados
     * @param categoriaId ID de la categoría
     * @return true si tiene productos asociados, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean tieneProductosAsociados(Long categoriaId) {
        return !productoRepository.findByCategoriaCategoriaId(categoriaId).isEmpty();
    }
    
    /**
     * Cuenta los productos asociados a una categoría
     * @param categoriaId ID de la categoría
     * @return número de productos asociados
     */
    @Transactional(readOnly = true)
    public long contarProductosAsociados(Long categoriaId) {
        return productoRepository.findByCategoriaCategoriaId(categoriaId).size();
    }
}
