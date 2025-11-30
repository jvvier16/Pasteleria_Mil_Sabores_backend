package com.example.Pasteleria_Mil_Sabores.Repository;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Obtener todos los productos con la categoría cargada (evita LazyInitializationException)
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria")
    List<Producto> findAllWithCategoria();
    
    // Obtener un producto por ID con la categoría cargada
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria WHERE p.productoId = :id")
    Optional<Producto> findByIdWithCategoria(@Param("id") Long id);
    
    // Para buscar productos cuyo nombre contiene la palabra clave (ignora mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por descripción
    List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Buscar por nombre o descripción
    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion);
    
    // Buscar por categoría
    List<Producto> findByCategoriaCategoriaId(Long categoriaId);
}