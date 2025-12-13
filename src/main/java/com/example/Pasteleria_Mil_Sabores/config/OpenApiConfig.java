package com.example.Pasteleria_Mil_Sabores.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de OpenAPI (Swagger) para la documentación de la API REST
 * 
 * Soporta dos métodos de autenticación:
 * 1. API Key (header X-API-Key)
 * 2. JWT Bearer Token (header Authorization)
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Definir esquema de seguridad para API Key
        SecurityScheme apiKeyScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("X-API-Key")
            .description("API Key para autenticación. Keys disponibles:\n" +
                "- Admin: CLAVE_SUPER_SECRETA_123\n" +
                "- Tester: TESTER_KEY_123\n" +
                "- Vendedor: VENDEDOR_KEY_123");

        // Definir esquema de seguridad para JWT Bearer Token
        SecurityScheme bearerScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("JWT Token obtenido del endpoint /api/v2/auth/login");

        // Crear componentes con los esquemas de seguridad
        Components components = new Components()
            .addSecuritySchemes("ApiKey", apiKeyScheme)
            .addSecuritySchemes("BearerAuth", bearerScheme);

        // Crear requisitos de seguridad (el usuario puede usar cualquiera)
        SecurityRequirement apiKeyRequirement = new SecurityRequirement().addList("ApiKey");
        SecurityRequirement bearerRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
            // Configura información básica del proyecto
            .info(new Info()
                .title("API Pastelería Mil Sabores")
                .version("v1")
                .description("API REST para la gestión de productos, categorías, usuarios y boletas de la pastelería.\n\n" +
                    "## Autenticación\n" +
                    "Esta API soporta dos métodos de autenticación:\n\n" +
                    "### 1. API Key\n" +
                    "Usa el header `X-API-Key` con una de las keys disponibles.\n\n" +
                    "### 2. JWT Token\n" +
                    "Obtén un token en `/api/v2/auth/login` y úsalo en el header `Authorization: Bearer <token>`")
                .contact(new Contact()
                    .name("Pastelería Mil Sabores")
                    .email("contacto@milsabores.cl"))
            )
            // Agregar componentes de seguridad
            .components(components)
            // Agregar requisitos de seguridad globales
            .security(Arrays.asList(apiKeyRequirement, bearerRequirement))
            // Define los servidores
            .servers(List.of(
                new Server().url("http://localhost:8094").description("Servidor Local (Desarrollo)"),
                new Server().url("http://localhost:8080").description("Servidor Alternativo")
            ));
    }
}
