package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // POST: Crear/Guardar una categoría
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // GET ALL: Obtener todas las categorías
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    // GET BY ID: Obtener una categoría por su ID
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // PUT: Actualizar una categoría existente
    public Categoria actualizarCategoria(Categoria categoriaActualizada) {
        Categoria existingCategoria = categoriaRepository.findById(categoriaActualizada.getCategoriaId()).orElse(null);
        if (existingCategoria != null) {
            existingCategoria.setNombre(categoriaActualizada.getNombre());
            existingCategoria.setDescripcion(categoriaActualizada.getDescripcion());
            // Nota: La relación @OneToMany (productos) debe manejarse en lógica de negocio si es necesario.
            return categoriaRepository.save(existingCategoria);
        }
        return null;
    }

    // DELETE: Eliminar una categoría por su ID
    public String eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
        return "Categoría con ID " + id + " eliminada.";
    }
}
