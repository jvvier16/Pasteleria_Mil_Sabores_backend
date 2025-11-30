package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import com.example.Pasteleria_Mil_Sabores.dto.CambioPasswordRequest;
import com.example.Pasteleria_Mil_Sabores.dto.PerfilDTO;
import com.example.Pasteleria_Mil_Sabores.dto.PerfilUpdateRequest;
import com.example.Pasteleria_Mil_Sabores.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * PerfilController - API v2 para gestión del perfil de usuario
 * 
 * Endpoints:
 * - GET /api/v2/perfil - Ver mi perfil
 * - PUT /api/v2/perfil - Actualizar mi perfil
 * - PUT /api/v2/perfil/password - Cambiar mi contraseña
 */
@RestController
@RequestMapping("/api/v2/perfil")
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public PerfilController(UsuarioRepository usuarioRepository, 
                           PasswordEncoder passwordEncoder, 
                           JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PerfilDTO>> obtenerMiPerfil(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            Usuario usuario = usuarioRepository.findById(userId).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(new PerfilDTO(usuario)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Error de autenticación: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PerfilDTO>> actualizarMiPerfil(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PerfilUpdateRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            Usuario usuario = usuarioRepository.findById(userId).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado"));
            }
            
            // Actualizar campos
            if (request.getNombre() != null) usuario.setNombre(request.getNombre());
            if (request.getApellido() != null) usuario.setApellido(request.getApellido());
            if (request.getTelefono() != null) usuario.setTelefono(request.getTelefono());
            if (request.getDireccion() != null) usuario.setDireccion(request.getDireccion());
            if (request.getImagen() != null) usuario.setImagen(request.getImagen());
            if (request.getFechaNacimiento() != null) usuario.setFechaNacimiento(request.getFechaNacimiento());
            
            Usuario actualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(ApiResponse.success("Perfil actualizado", new PerfilDTO(actualizado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error al actualizar: " + e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> cambiarPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CambioPasswordRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token inválido"));
            }
            
            Usuario usuario = usuarioRepository.findById(userId).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado"));
            }
            
            // Validar contraseña actual
            boolean passwordMatch = usuario.getContrasena().startsWith("$2") 
                ? passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())
                : usuario.getContrasena().equals(request.getContrasenaActual());
            
            if (!passwordMatch) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("Contraseña actual incorrecta"));
            }
            
            if (request.getContrasenaNueva() == null || request.getContrasenaNueva().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("La nueva contraseña debe tener al menos 6 caracteres"));
            }
            
            usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
            usuarioRepository.save(usuario);
            
            return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("Error: " + e.getMessage()));
        }
    }
}

