package com.example.Pasteleria_Mil_Sabores.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración adicional de CORS para WebMvc
 * Funciona como respaldo junto con la configuración de SecurityConfig
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
            // Permitir todos los orígenes
            .allowedOrigins("*")
            // Métodos HTTP permitidos
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
            // Headers permitidos (incluir X-API-Key)
            .allowedHeaders(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Cache-Control",
                "Pragma",
                "X-API-Key",
                "Api-Key",
                "x-api-key",
                "api-key"
            )
            // Headers expuestos
            .exposedHeaders(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods"
            )
            // NO usar credenciales con "*"
            .allowCredentials(false)
            // Cache de preflight
            .maxAge(3600);
    }
}
