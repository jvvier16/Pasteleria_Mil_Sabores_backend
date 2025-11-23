package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    // POST: Crear/Guardar una boleta (Esto representa la finalización de una compra)
    public Boleta crearBoleta(Boleta boleta) {
        // Lógica clave: Calcular el total, guardar detalles de boleta, integrar pago (sandbox),
        // y descontar stock de los productos.
        return boletaRepository.save(boleta);
    }
    
    // GET ALL: Obtener todas las boletas (principalmente para el admin)
    public List<Boleta> obtenerTodasLasBoletas() {
        return boletaRepository.findAll();
    }

    // GET BY ID: Obtener una boleta por su ID
    public Boleta obtenerBoletaPorId(Long id) {
        return boletaRepository.findById(id).orElse(null);
    }
    
    // Podrías necesitar: List<Boleta> obtenerBoletasPorUsuario(Integer userId);
}
