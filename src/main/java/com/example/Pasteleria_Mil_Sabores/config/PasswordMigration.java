package com.example.Pasteleria_Mil_Sabores.config;

import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PasswordMigration - Migra contraseñas en texto plano a BCrypt
 * 
 * Este componente se ejecuta al iniciar la aplicación y verifica si hay
 * contraseñas que no están hasheadas. Si encuentra alguna, la hashea
 * automáticamente con BCrypt.
 * 
 * Las contraseñas hasheadas con BCrypt comienzan con "$2a$", "$2b$" o "$2y$",
 * por lo que es fácil identificar cuáles necesitan migración.
 * 
 * Se ejecuta DESPUÉS del DataLoader (Order 2) para asegurar que los usuarios
 * ya estén cargados.
 */
@Component
@Order(2) // Se ejecuta después del DataLoader
public class PasswordMigration implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordMigration(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🔐 Verificando contraseñas...");
        
        List<Usuario> usuarios = usuarioRepository.findAll();
        int migradas = 0;
        
        for (Usuario usuario : usuarios) {
            String contrasena = usuario.getContrasena();
            
            // Verificar si la contraseña NO está hasheada con BCrypt
            // BCrypt hashes siempre empiezan con $2a$, $2b$ o $2y$
            if (contrasena != null && !esBCryptHash(contrasena)) {
                // Hashear la contraseña
                String hashedPassword = passwordEncoder.encode(contrasena);
                usuario.setContrasena(hashedPassword);
                usuarioRepository.save(usuario);
                migradas++;
                System.out.println("   ✅ Contraseña migrada para: " + usuario.getCorreo());
            }
        }
        
        if (migradas > 0) {
            System.out.println("🔐 Migración completada: " + migradas + " contraseñas hasheadas con BCrypt");
        } else {
            System.out.println("✅ Todas las contraseñas ya están hasheadas con BCrypt");
        }
    }

    /**
     * Verifica si una contraseña ya está hasheada con BCrypt
     * 
     * @param password La contraseña a verificar
     * @return true si es un hash BCrypt, false si es texto plano
     */
    private boolean esBCryptHash(String password) {
        // BCrypt hashes tienen formato: $2a$10$... o $2b$10$... o $2y$10$...
        // Y siempre tienen 60 caracteres de longitud
        return password != null && 
               password.length() == 60 && 
               (password.startsWith("$2a$") || 
                password.startsWith("$2b$") || 
                password.startsWith("$2y$"));
    }
}






