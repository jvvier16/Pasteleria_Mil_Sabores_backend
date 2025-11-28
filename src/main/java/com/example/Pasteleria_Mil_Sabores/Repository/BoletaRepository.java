package com.example.Pasteleria_Mil_Sabores.Repository;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    // Obtener boletas por usuario
    List<Boleta> findByClienteUserId(Integer userId);
    
    // Obtener boletas por estado
    List<Boleta> findByEstado(String estado);
}
