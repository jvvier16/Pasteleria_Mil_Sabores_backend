package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Service.UsuarioService;
import com.example.Pasteleria_Mil_Sabores.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // POST (Registro): http://localhost:8094/api/v1/usuarios
    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario creado = service.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(creado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    // GET ALL (Admin): http://localhost:8094/api/v1/usuarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = service.obtenerTodosLosUsuarios();
            return ResponseEntity.ok(ApiResponse.success(usuarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener usuarios: " + e.getMessage()));
        }
    }

    // GET BY ID: http://localhost:8094/api/v1/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> obtenerUsuarioPorId(@PathVariable Integer id) {
        try {
            Usuario usuario = service.obtenerUsuarioPorId(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado con ID: " + id));
            }
            return ResponseEntity.ok(ApiResponse.success(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al obtener usuario: " + e.getMessage()));
        }
    }

    // PUT: http://localhost:8094/api/v1/usuarios
    @PutMapping
    public ResponseEntity<ApiResponse<Usuario>> actualizarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario actualizado = service.actualizarUsuario(usuario);
            if (actualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado"));
            }
            return ResponseEntity.ok(ApiResponse.success("Usuario actualizado", actualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    // DELETE (Admin): http://localhost:8094/api/v1/usuarios/1
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarUsuario(@PathVariable Integer id) {
        try {
            String resultado = service.eliminarUsuario(id);
            if (resultado.contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound(resultado));
            }
            return ResponseEntity.ok(ApiResponse.success(resultado, "ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error al eliminar usuario: " + e.getMessage()));
        }
    }
}
