package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.LoginRequest;
import com.example.Pasteleria_Mil_Sabores.dto.LoginResponse;
import com.example.Pasteleria_Mil_Sabores.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - Maneja autenticación de usuarios
 * 
 * Endpoints:
 * - POST /api/v1/auth/login - Login de usuario
 * - POST /api/v1/auth/registro - Registro de nuevo usuario
 * - POST /api/v2/auth/login - Login (versión pública)
 * - POST /api/v2/auth/registro - Registro (versión pública)
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/v1/auth/login
     * POST /api/v2/auth/login
     * 
     * Autentica un usuario y devuelve un token JWT
     */
    @PostMapping({"/v1/auth/login", "/v2/auth/login"})
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo()).orElse(null);
        
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Credenciales inválidas"));
        }

        // Verificar contraseña (soporta BCrypt y texto plano para migración)
        boolean passwordMatch = false;
        if (usuario.getContrasena().startsWith("$2")) {
            // Contraseña encriptada con BCrypt
            passwordMatch = passwordEncoder.matches(request.getContrasena(), usuario.getContrasena());
        } else {
            // Contraseña en texto plano (legacy)
            passwordMatch = usuario.getContrasena().equals(request.getContrasena());
        }

        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Credenciales inválidas"));
        }

        // Verificar si usuario está activo
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Usuario desactivado"));
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(
            usuario.getCorreo(), 
            usuario.getRole() != null ? usuario.getRole() : "user",
            usuario.getUserId()
        );

        // Crear respuesta
        String nombreCompleto = (usuario.getNombre() != null ? usuario.getNombre() : "") + 
                               (usuario.getApellido() != null ? " " + usuario.getApellido() : "");
        
        LoginResponse response = new LoginResponse(
            token,
            usuario.getUserId(),
            nombreCompleto.trim(),
            usuario.getCorreo(),
            usuario.getRole()
        );

        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    /**
     * POST /api/v1/auth/registro
     * POST /api/v2/auth/registro
     * 
     * Registra un nuevo usuario
     */
    @PostMapping({"/v1/auth/registro", "/v2/auth/registro"})
    public ResponseEntity<ApiResponse<LoginResponse>> registro(@Valid @RequestBody Usuario usuario) {
        
        // Verificar si el correo ya existe
        Usuario existente = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (existente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "El correo ya está registrado"));
        }

        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        
        // Asignar rol por defecto si no tiene
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("user");
        }

        // Activar usuario
        usuario.setActivo(true);

        // Guardar usuario
        Usuario guardado = usuarioRepository.save(usuario);

        // Generar token JWT automáticamente
        String token = jwtUtil.generateToken(
            guardado.getCorreo(),
            guardado.getRole(),
            guardado.getUserId()
        );

        String nombreCompleto = (guardado.getNombre() != null ? guardado.getNombre() : "") + 
                               (guardado.getApellido() != null ? " " + guardado.getApellido() : "");

        LoginResponse response = new LoginResponse(
            token,
            guardado.getUserId(),
            nombreCompleto.trim(),
            guardado.getCorreo(),
            guardado.getRole()
        );
        response.setMensaje("Registro exitoso");

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(response));
    }

    /**
     * GET /api/v1/auth/verificar
     * GET /api/v2/auth/verificar
     * 
     * Verifica si el token JWT sigue siendo válido
     */
    @GetMapping({"/v1/auth/verificar", "/v2/auth/verificar"})
    public ResponseEntity<ApiResponse<VerificacionResponse>> verificarToken(
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token no proporcionado"));
            }
            
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);
            
            Usuario usuario = usuarioRepository.findByCorreo(email).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Usuario no encontrado"));
            }
            
            if (!jwtUtil.isTokenValid(token, email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token expirado o inválido"));
            }
            
            if (usuario.getActivo() != null && !usuario.getActivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "Usuario desactivado"));
            }
            
            VerificacionResponse response = new VerificacionResponse(
                true, usuario.getUserId(), usuario.getCorreo(), usuario.getRole()
            );
            
            return ResponseEntity.ok(ApiResponse.success("Token válido", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Token inválido"));
        }
    }

    /**
     * POST /api/v1/auth/logout
     * POST /api/v2/auth/logout
     * 
     * Cierra la sesión (el cliente debe eliminar el token)
     */
    @PostMapping({"/v1/auth/logout", "/v2/auth/logout"})
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Sesión cerrada. Elimina el token del almacenamiento local.", null));
    }

    // DTO interno para verificación
    public static class VerificacionResponse {
        private boolean valido;
        private Integer userId;
        private String correo;
        private String role;

        public VerificacionResponse(boolean valido, Integer userId, String correo, String role) {
            this.valido = valido;
            this.userId = userId;
            this.correo = correo;
            this.role = role;
        }

        public boolean isValido() { return valido; }
        public Integer getUserId() { return userId; }
        public String getCorreo() { return correo; }
        public String getRole() { return role; }
    }
}

