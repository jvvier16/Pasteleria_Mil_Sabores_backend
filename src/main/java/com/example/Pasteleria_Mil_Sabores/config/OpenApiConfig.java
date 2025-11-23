package com.example.Pasteleria_Mil_Sabores.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//Bean de configuración para OpenAPI (Swagger) para la documentación de la API REST
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            // Configura información básica del proyecto
            .info(new Info()
                .title("API Pastelería Mil Sabores")
                .version("v1")
                .description("API REST para la gestión de productos, categorías, usuarios y boletas de la pastelería.")
            )
            // Define el servidor (útil si se despliega en otro lugar)
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor Local (Desarrollo)")
            ));
    }
}