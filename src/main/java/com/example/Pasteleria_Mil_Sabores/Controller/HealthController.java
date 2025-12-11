package com.example.Pasteleria_Mil_Sabores.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HealthController - Endpoint para verificar el estado del servidor
 * 
 * Usado por el frontend para detectar si la API está disponible
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * GET /api/health
     * 
     * Retorna el estado del servidor
     * Endpoint público (no requiere autenticación)
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "Pasteleria Mil Sabores API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
}




