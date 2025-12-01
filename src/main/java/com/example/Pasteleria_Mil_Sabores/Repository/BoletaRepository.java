package com.example.Pasteleria_Mil_Sabores.Repository;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    
    // Obtener boletas por usuario con cliente y items cargados
    @Query("SELECT DISTINCT b FROM Boleta b " +
           "LEFT JOIN FETCH b.cliente " +
           "LEFT JOIN FETCH b.items i " +
           "LEFT JOIN FETCH i.producto " +
           "WHERE b.cliente.userId = :userId " +
           "ORDER BY b.fecha DESC")
    List<Boleta> findByClienteUserIdWithDetails(@Param("userId") Integer userId);
    
    // Obtener boleta por ID con todas las relaciones
    @Query("SELECT b FROM Boleta b " +
           "LEFT JOIN FETCH b.cliente " +
           "LEFT JOIN FETCH b.items i " +
           "LEFT JOIN FETCH i.producto " +
           "WHERE b.boletaId = :id")
    Optional<Boleta> findByIdWithDetails(@Param("id") Long id);
    
    // Obtener boletas por estado con detalles
    @Query("SELECT DISTINCT b FROM Boleta b " +
           "LEFT JOIN FETCH b.cliente " +
           "LEFT JOIN FETCH b.items i " +
           "LEFT JOIN FETCH i.producto " +
           "WHERE b.estado = :estado " +
           "ORDER BY b.fecha DESC")
    List<Boleta> findByEstadoWithDetails(@Param("estado") String estado);
    
    // Obtener todas las boletas con detalles
    @Query("SELECT DISTINCT b FROM Boleta b " +
           "LEFT JOIN FETCH b.cliente " +
           "LEFT JOIN FETCH b.items i " +
           "LEFT JOIN FETCH i.producto " +
           "ORDER BY b.fecha DESC")
    List<Boleta> findAllWithDetails();
    
    // Métodos simples (sin fetch join) para casos donde no se necesita
    List<Boleta> findByClienteUserId(Integer userId);
    List<Boleta> findByEstado(String estado);
}
