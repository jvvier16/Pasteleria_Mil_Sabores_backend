package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // POST: Crear/Registrar un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        // En el futuro, aquí se debe encriptar la contraseña (BCrypt) antes de guardar.
        // Checklist: cuidar las contraseñas (encriptalas antes de mandar a bdd con bcript)
        return usuarioRepository.save(usuario);
    }

    // GET ALL: Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // GET BY ID: Obtener un usuario por su ID
    public Usuario obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    // GET by Correo (Necesario para JWT/Login)
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // PUT: Actualizar un usuario existente
    public Usuario actualizarUsuario(Usuario usuarioActualizado) {
        Usuario existingUsuario = usuarioRepository.findById(usuarioActualizado.getUserId()).orElse(null);
        if (existingUsuario != null) {
            existingUsuario.setNombre(usuarioActualizado.getNombre());
            existingUsuario.setApellido(usuarioActualizado.getApellido());
            existingUsuario.setDireccion(usuarioActualizado.getDireccion());
            existingUsuario.setTelefono(usuarioActualizado.getTelefono());
            existingUsuario.setRole(usuarioActualizado.getRole());
            // No se actualiza la contraseña directamente aquí, usaría un método dedicado.
            return usuarioRepository.save(existingUsuario);
        }
        return null;
    }

    // DELETE: Eliminar un usuario por su ID
    public String eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
        return "Usuario con ID " + id + " eliminado.";
    }
}
