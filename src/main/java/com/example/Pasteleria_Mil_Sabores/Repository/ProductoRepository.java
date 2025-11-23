package com.example.Pasteleria_Mil_Sabores.Repository;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Para buscar productos cuyo nombre contiene la palabra clave (ignora mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}