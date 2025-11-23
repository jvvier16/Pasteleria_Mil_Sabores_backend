package com.example.Pasteleria_Mil_Sabores.Controller;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // POST (Registro): http://localhost:8080/api/v1/usuarios
    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        // NOTA: Recuerda que aquí debe aplicarse el BCrypt en el Service antes de guardar.
        return service.crearUsuario(usuario);
    }

    // GET ALL (Admin): http://localhost:8080/api/v1/usuarios
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return service.obtenerTodosLosUsuarios();
    }

    // GET BY ID: http://localhost:8080/api/v1/usuarios/1
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Integer id) {
        return service.obtenerUsuarioPorId(id);
    }

    // PUT: http://localhost:8080/api/v1/usuarios
    @PutMapping
    public Usuario actualizarUsuario(@RequestBody Usuario usuario) {
        return service.actualizarUsuario(usuario);
    }

    // DELETE (Admin): http://localhost:8080/api/v1/usuarios/1
    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        return service.eliminarUsuario(id);
    }
}
