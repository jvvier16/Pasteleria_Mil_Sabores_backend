package com.example.Pasteleria_Mil_Sabores.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Filtro de autenticación que soporta:
 * 1. JWT Token (Authorization: Bearer <token>)
 * 2. API Key (X-API-Key: <key> o Api-Key: <key>)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    @Value("${milsabores.admin.api-key:}")
    private String adminApiKey;
    
    // API Keys adicionales pueden ser configuradas aquí o en application.properties
    @Value("${milsabores.tester.api-key:TESTER_KEY_123}")
    private String testerApiKey;
    
    @Value("${milsabores.vendedor.api-key:VENDEDOR_KEY_123}")
    private String vendedorApiKey;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Permitir solicitudes OPTIONS (preflight CORS) sin procesar
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }
        
        // Si ya hay autenticación, continuar
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 1. Intentar autenticación por API Key primero
        if (tryApiKeyAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 2. Si no hay API Key válida, intentar JWT
        tryJwtAuthentication(request);
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Intenta autenticar usando API Key
     * Soporta headers: X-API-Key, Api-Key, x-api-key
     * @return true si la autenticación fue exitosa
     */
    private boolean tryApiKeyAuthentication(HttpServletRequest request) {
        // Buscar API Key en diferentes headers
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getHeader("Api-Key");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getHeader("x-api-key");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getHeader("api-key");
        }
        
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }
        
        // Validar API Key y obtener rol
        String role = validateApiKey(apiKey);
        if (role == null) {
            logger.warn("API Key inválida recibida");
            return false;
        }
        
        // Crear autenticación con el rol correspondiente
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
        );
        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            "api-key-user-" + role.toLowerCase(),
            null,
            authorities
        );
        
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        logger.debug("Autenticación por API Key exitosa con rol: " + role);
        return true;
    }
    
    /**
     * Valida la API Key y retorna el rol asociado
     * @return el rol (ADMIN, TESTER, VENDEDOR) o null si la key es inválida
     */
    private String validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }
        
        // Validar contra las API keys configuradas
        if (adminApiKey != null && !adminApiKey.isEmpty() && adminApiKey.equals(apiKey)) {
            return "ADMIN";
        }
        if (testerApiKey != null && !testerApiKey.isEmpty() && testerApiKey.equals(apiKey)) {
            return "TESTER";
        }
        if (vendedorApiKey != null && !vendedorApiKey.isEmpty() && vendedorApiKey.equals(apiKey)) {
            return "VENDEDOR";
        }
        
        return null;
    }
    
    /**
     * Intenta autenticar usando JWT Token
     */
    private void tryJwtAuthentication(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = jwtUtil.extractEmail(jwt);
            final String role = jwtUtil.extractRole(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Crear authority con el rol
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(authority)
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token inválido, continuar sin autenticación
            logger.error("Error procesando JWT: " + e.getMessage());
        }
    }
}
