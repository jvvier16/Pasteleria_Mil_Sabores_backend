package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    // POST: Crear/Guardar una boleta (Esto representa la finalización de una compra)
    @NonNull
    public Boleta crearBoleta(@NonNull Boleta boleta) {
        // Lógica clave: Calcular el total, guardar detalles de boleta, integrar pago (sandbox),
        // y descontar stock de los productos.
        return boletaRepository.save(boleta);
    }
    
    // GET ALL: Obtener todas las boletas (principalmente para el admin)
    public List<Boleta> obtenerTodasLasBoletas() {
        return boletaRepository.findAll();
    }

    // GET BY ID: Obtener una boleta por su ID
    public Boleta obtenerBoletaPorId(@NonNull Long id) {
        return boletaRepository.findById(id).orElse(null);
    }
    
    // GET BY USER: Obtener boletas de un usuario específico
    public List<Boleta> obtenerBoletasPorUsuario(@NonNull Integer userId) {
        return boletaRepository.findByClienteUserId(userId);
    }
    
    // GET BY ESTADO: Obtener boletas por estado
    public List<Boleta> obtenerBoletasPorEstado(String estado) {
        return boletaRepository.findByEstado(estado);
    }
    
    // PUT: Actualizar estado de una boleta
    public Boleta actualizarEstadoBoleta(@NonNull Long id, String nuevoEstado) {
        Boleta boleta = boletaRepository.findById(id).orElse(null);
        if (boleta != null) {
            boleta.setEstado(nuevoEstado);
            return boletaRepository.save(boleta);
        }
        return null;
    }
    
    // DELETE: Eliminar una boleta
    public String eliminarBoleta(@NonNull Long id) {
        if (boletaRepository.existsById(id)) {
            boletaRepository.deleteById(id);
            return "Boleta con ID " + id + " eliminada.";
        }
        return "Boleta no encontrada.";
    }
}
