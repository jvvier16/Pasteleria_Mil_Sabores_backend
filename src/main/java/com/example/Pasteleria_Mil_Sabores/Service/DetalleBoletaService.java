package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.DetalleBoleta;
import com.example.Pasteleria_Mil_Sabores.Repository.DetalleBoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DetalleBoletaService {

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    // POST: Crear/Guardar un detalle de boleta.
    // NOTA: En la práctica, los detalles se guardan implícitamente dentro del método
    //       crearBoleta() en BoletaService, ya que son parte de la transacción.
    public DetalleBoleta crearDetalleBoleta(DetalleBoleta detalle) {
        return detalleBoletaRepository.save(detalle);
    }

    // GET ALL: Obtener todos los detalles de boleta (generalmente solo para admin/reportes)
    public List<DetalleBoleta> obtenerTodosLosDetalles() {
        return detalleBoletaRepository.findAll();
    }
    
    // GET BY ID: Obtener un detalle de boleta por su ID
    public DetalleBoleta obtenerDetallePorId(Long id) {
        return detalleBoletaRepository.findById(id).orElse(null);
    }

    // Podrías necesitar: List<DetalleBoleta> obtenerDetallesPorBoletaId(Long boletaId);
}
