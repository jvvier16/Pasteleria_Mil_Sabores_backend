package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.DetalleBoleta;
import com.example.Pasteleria_Mil_Sabores.Service.DetalleBoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles")
public class DetalleBoletaController {

    @Autowired
    private DetalleBoletaService service;

    // POST (Generalmente no se llama directamente, los Detalles se crean con la Boleta)
    @PostMapping
    public DetalleBoleta crearDetalleBoleta(@RequestBody DetalleBoleta detalle) {
        return service.crearDetalleBoleta(detalle);
    }

    // GET ALL (Admin/Reportes): http://localhost:8080/api/v1/detalles
    @GetMapping
    public List<DetalleBoleta> obtenerTodosLosDetalles() {
        return service.obtenerTodosLosDetalles();
    }

    // GET BY ID: http://localhost:8080/api/v1/detalles/1
    @GetMapping("/{id}")
    public DetalleBoleta obtenerDetallePorId(@PathVariable Long id) {
        return service.obtenerDetallePorId(id);
    }
}
