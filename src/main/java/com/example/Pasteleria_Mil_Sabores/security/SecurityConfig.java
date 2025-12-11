package com.example.Pasteleria_Mil_Sabores.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // === PERMITIR TODAS LAS SOLICITUDES OPTIONS (preflight CORS) ===
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // === ENDPOINTS PÚBLICOS (sin autenticación) ===
                // Health Check (para verificar disponibilidad del servidor)
                .requestMatchers("/api/health").permitAll()
                
                // Auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v2/auth/**").permitAll()
                
                // Swagger/OpenAPI
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // === API v2 - PÚBLICA (según Excel) ===
                .requestMatchers(HttpMethod.GET, "/api/v2/productos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/productos/buscar").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/productos/categoria/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/categorias").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/categorias/**").permitAll()
                
                // === API v2 - Requiere ADMIN o TESTER ===
                .requestMatchers(HttpMethod.POST, "/api/v2/productos").hasAnyRole("ADMIN", "TESTER", "VENDEDOR")
                .requestMatchers(HttpMethod.PUT, "/api/v2/productos/**").hasAnyRole("ADMIN", "TESTER", "VENDEDOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v2/productos/**").hasAnyRole("ADMIN", "TESTER")
                
                // === API v1 - PRIVADA (ADMIN, TESTER, VENDEDOR según rol) ===
                // Estadísticas y Reportes - Solo Admin y Tester
                .requestMatchers("/api/v1/estadisticas").hasAnyRole("ADMIN", "TESTER")
                .requestMatchers("/api/v1/reportes/**").hasAnyRole("ADMIN", "TESTER")
                
                // Productos - Admin y Tester pueden gestionar
                .requestMatchers("/api/v1/productos/**").hasAnyRole("ADMIN", "TESTER", "VENDEDOR")
                
                // Categorías - Admin y Tester
                .requestMatchers("/api/v1/categorias/**").hasAnyRole("ADMIN", "TESTER")
                
                // Usuarios - Solo Admin y Tester
                .requestMatchers("/api/v1/usuarios/**").hasAnyRole("ADMIN", "TESTER")
                
                // Boletas - Admin, Tester y Vendedor pueden ver/gestionar
                .requestMatchers("/api/v1/boletas/**").hasAnyRole("ADMIN", "TESTER", "VENDEDOR")
                
                // Detalles - Admin y Tester
                .requestMatchers("/api/v1/detalles/**").hasAnyRole("ADMIN", "TESTER")
                
                // === Endpoints para usuarios autenticados (cualquier rol) ===
                // Boletas v2
                .requestMatchers("/api/v2/boletas/mis-pedidos").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v2/boletas").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v2/boletas/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v2/boletas/*/cancelar").authenticated()
                
                // Perfil
                .requestMatchers("/api/v2/perfil/**").authenticated()
                
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Usar allowedOriginPatterns para poder usar wildcards con credenciales
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://localhost:*",
            "https://127.0.0.1:*",
            "*"
        ));
        
        // Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "Cache-Control",
            "Pragma"
        ));
        
        // Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Permitir credenciales (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Tiempo máximo de cache para preflight (en segundos)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
