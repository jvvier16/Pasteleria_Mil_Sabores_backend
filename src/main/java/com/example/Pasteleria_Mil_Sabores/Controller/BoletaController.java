package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    @Autowired
    private BoletaService service;

    // POST (Finalizar Compra): http://localhost:8080/api/v1/boletas
    @PostMapping
    public Boleta crearBoleta(@RequestBody Boleta boleta) {
        // NOTA: Esta es la lógica de negocio más compleja (stock, total, pago).
        return service.crearBoleta(boleta);
    }

    // GET ALL (Admin): http://localhost:8080/api/v1/boletas
    @GetMapping
    public List<Boleta> obtenerTodasLasBoletas() {
        return service.obtenerTodasLasBoletas();
    }

    // GET BY ID: http://localhost:8080/api/v1/boletas/1
    @GetMapping("/{id}")
    public Boleta obtenerBoletaPorId(@PathVariable Long id) {
        return service.obtenerBoletaPorId(id);
    }
}
