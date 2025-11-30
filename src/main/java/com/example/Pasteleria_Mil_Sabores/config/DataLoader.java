package com.example.Pasteleria_Mil_Sabores.config;

import com.example.Pasteleria_Mil_Sabores.Entity.Categoria;
import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.CategoriaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DataLoader - Carga automática de datos iniciales
 * 
 * Este componente se ejecuta automáticamente al iniciar la aplicación.
 * Verifica si las tablas están vacías y, de ser así, carga los datos
 * iniciales de categorías, usuarios y productos.
 * 
 * Las contraseñas de los usuarios se hashean automáticamente con BCrypt.
 * 
 * Se ejecuta PRIMERO (Order 1) antes de la migración de contraseñas.
 */
@Component
@Order(1) // Se ejecuta primero
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CategoriaRepository categoriaRepository,
                      UsuarioRepository usuarioRepository,
                      ProductoRepository productoRepository,
                      PasswordEncoder passwordEncoder) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🌱 Verificando datos iniciales...");
        
        // Solo cargar si las tablas están vacías
        boolean categoriasVacias = categoriaRepository.count() == 0;
        boolean usuariosVacios = usuarioRepository.count() == 0;
        boolean productosVacios = productoRepository.count() == 0;

        if (categoriasVacias) {
            cargarCategorias();
        } else {
            System.out.println("✅ Categorías ya existen (" + categoriaRepository.count() + " registros)");
        }

        if (usuariosVacios) {
            cargarUsuarios();
        } else {
            System.out.println("✅ Usuarios ya existen (" + usuarioRepository.count() + " registros)");
        }

        if (productosVacios) {
            cargarProductos();
        } else {
            System.out.println("✅ Productos ya existen (" + productoRepository.count() + " registros)");
        }

        System.out.println("🚀 Carga de datos inicial completada!");
    }

    private void cargarCategorias() {
        System.out.println("📦 Cargando categorías...");

        String[][] categorias = {
            {"Tortas", "Deliciosas tortas para toda ocasión"},
            {"Postres", "Postres individuales y porciones"},
            {"Sin Azúcar", "Opciones saludables sin azúcar añadida"},
            {"Otros", "Variedad de productos de pastelería"},
            {"Sin Gluten", "Productos aptos para celíacos"},
            {"Veganas", "Productos 100% veganos"},
            {"Especiales", "Tortas para ocasiones especiales"}
        };

        for (String[] cat : categorias) {
            Categoria categoria = new Categoria();
            categoria.setNombre(cat[0]);
            categoria.setDescripcion(cat[1]);
            categoriaRepository.save(categoria);
        }

        System.out.println("✅ " + categorias.length + " categorías cargadas");
    }

    private void cargarUsuarios() {
        System.out.println("👥 Cargando usuarios...");

        // Datos de usuarios: {nombre, apellido, correo, contrasena, role, fechaNacimiento, direccion, imagen}
        Object[][] usuarios = {
            {"Ana", "García", "ana.garcia@gmail.com", "AnaGarcia1234", "user", "1990-05-14", "Ñuñoa, Santiago", null},
            {"Luis", "Martínez", "luis.martinez@gmail.com", "LuisMartinez1", "user", "1987-09-02", "La Florida, Santiago", null},
            {"Sofía", "López", "sofia.lopez@gmail.com", "SofiaLopez12", "user", "1996-11-21", "Providencia, Santiago", null},
            {"Carlos", "Ruiz", "carlos.ruiz@gmail.com", "CarlosRuiz34", "user", "1979-04-08", "Maipú, Santiago", null},
            {"Valentina", "Torres", "valentina.torres@gmail.com", "ValenTorres56", "user", "1984-12-30", "Las Condes, Santiago", null},
            {"Javier", "Rojas", "javier.rojas@gmail.com", "JavierRojas13", "tester", "2005-11-14", "Puente Alto, Santiago", "../assets/img/segunda.jpeg"},
            {"admin", "tienda", "admin@gmail.com", "JavierRojas12", "admin", "1990-01-01", "Santiago, Chile", null},
            {"aracelly", "zenteno", "aracelly.zenteno@gmail.com", "Aracellyzen90", "vendedor", "2006-03-23", "Puente Alto, Santiago", "../assets/img/primera.jpeg"},
            {"Matías", "Jara", "matias.jara@gmail.com", "MatiasJara22", "admin", "2005-04-08", "Puente Alto, Santiago", "../assets/img/tecera.jpeg"},
            {"Fernanda", "Castillo", "fernanda.castillo@gmail.com", "FerCastillo45", "user", "1978-02-17", "Providencia, Santiago", null},
            {"Ricardo", "Morales", "ricardo.morales@gmail.com", "RicaMorales80", "user", "1960-06-29", "Maipú, Santiago", null},
            {"Daniela", "Vega", "daniela.vega@gmail.com", "DanielaVega19", "user", "2003-03-15", "Puente Alto, Santiago", null},
            {"Tomás", "Aguilera", "tomas.aguilera@gmail.com", "TomasAgui007", "user", "1985-09-01", "Las Condes, Santiago", null},
            {"Beatriz", "Fuentes", "beatriz.fuentes@gmail.com", "BeaFuent5555", "user", "1970-07-21", "Recoleta, Santiago", null},
            {"Gabriel", "Navarro", "gabriel.navarro@gmail.com", "GabiNava4444", "user", "1980-12-09", "San Miguel, Santiago", null},
            {"Francisca", "Silva", "francisca.silva@gmail.com", "FranSilva067", "user", "1957-08-14", "La Reina, Santiago", null},
            {"Ignacio", "Vergara", "ignacio.vergara@gmail.com", "IgnacioVer03", "user", "1999-10-30", "Quilicura, Santiago", null},
            {"Paula", "Mendoza", "paula.mendoza@gmail.com", "PaulaMend012", "user", "1992-01-22", "Independencia, Santiago", null},
            {"Héctor", "Carrasco", "hector.carrasco@gmail.com", "HectorCar050", "user", "1974-05-27", "Peñalolén, Santiago", null},
            {"Natalia", "Ramos", "natalia.ramos@gmail.com", "NataRamos008", "user", "1986-09-19", "Santiago Centro, Santiago", null}
        };

        for (Object[] u : usuarios) {
            Usuario usuario = new Usuario();
            usuario.setNombre((String) u[0]);
            usuario.setApellido((String) u[1]);
            usuario.setCorreo((String) u[2]);
            // ¡IMPORTANTE! Hasheamos la contraseña con BCrypt
            usuario.setContrasena(passwordEncoder.encode((String) u[3]));
            usuario.setRole((String) u[4]);
            usuario.setFechaNacimiento(LocalDate.parse((String) u[5]));
            usuario.setDireccion((String) u[6]);
            usuario.setImagen((String) u[7]);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        }

        System.out.println("✅ " + usuarios.length + " usuarios cargados (contraseñas hasheadas con BCrypt)");
    }

    private void cargarProductos() {
        System.out.println("🎂 Cargando productos...");

        // Primero obtenemos las categorías para asignarlas
        Map<String, Categoria> categoriasMap = new HashMap<>();
        categoriaRepository.findAll().forEach(c -> categoriasMap.put(c.getNombre(), c));

        // Datos de productos: {nombre, precio, stock, imagen, categoriaNombre, descripcion}
        Object[][] productos = {
            {"Torta de chocolate cuadrada", 45000, 10, "../assets/img/Torta Cuadrada de Chocolate.webp", "Tortas", "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas."},
            {"Torta Cuadrada de Frutas", 50000, 10, "../assets/img/Torta Cuadrada de Frutas.jpeg", "Tortas", "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho."},
            {"Torta Circular de Vainilla", 35000, 15, "../assets/img/torta circular de vainilla.jpeg", "Tortas", "Clásica torta de vainilla con frosting de crema y decoración elegante."},
            {"Torta Circular de Frutas", 38000, 12, "../assets/img/Torta Circular de Frutas.jpeg", "Tortas", "Bizcocho suave cubierto de frutas de temporada y brillo de mermelada."},
            {"Torta Circular de Manjar", 42000, 8, "../assets/img/Torta Circular de Manjar.jpeg", "Tortas", "Dulce torta rellena de manjar con decoración de caramelo."},
            {"Torta Tres Leches Tradicional", 40000, 10, "../assets/img/Torta Tres Leches Tradicional.jpeg", "Tortas", "Esponjosa y húmeda torta bañada en tres tipos de leche."},
            {"Torta Red Velvet", 48000, 7, "../assets/img/Torta Red Velvet.jpeg", "Tortas", "Torta roja aterciopelada con frosting de queso crema."},
            {"Torta Especial de Cumpleaños", 55000, 5, "../assets/img/Torta Especial de Cumpleaños.jpeg", "Especiales", "Torta personalizable ideal para celebraciones de cumpleaños."},
            {"Torta Especial de Boda", 120000, 3, "../assets/img/Torta Especial de Boda.jpeg", "Especiales", "Elegante torta de varios pisos para el día más especial."},
            {"Tiramisú Clásico", 28000, 15, "../assets/img/Tiramisú Clásico.jpeg", "Postres", "Postre italiano con capas de café, mascarpone y cacao."},
            {"Mousse de Chocolate", 22000, 20, "../assets/img/Mousse de Chocolate.jpeg", "Postres", "Suave mousse de chocolate belga con textura aireada."},
            {"Pie de Limón", 25000, 12, "../assets/img/Pie de Limón.jpeg", "Postres", "Base crocante con relleno de limón y merengue dorado."},
            {"Cheesecake Sin Azúcar", 32000, 8, "../assets/img/Cheesecake Sin Azúcar.jpeg", "Sin Azúcar", "Cremoso cheesecake endulzado con stevia, apto para diabéticos."},
            {"Torta Sin Azúcar de Naranja", 35000, 6, "../assets/img/Torta Sin Azúcar de Naranja.jpeg", "Sin Azúcar", "Torta de naranja natural sin azúcar añadida."},
            {"Brownie Sin Gluten", 18000, 25, "../assets/img/Brownie Sin Gluten.jpeg", "Sin Gluten", "Brownie de chocolate intenso hecho con harina de almendras."},
            {"Pan Sin Gluten", 12000, 30, "../assets/img/Pan Sin Gluten.jpeg", "Sin Gluten", "Pan artesanal sin gluten, perfecto para celíacos."},
            {"Torta Vegana de Chocolate", 45000, 8, "../assets/img/Torta Vegana de Chocolate.jpeg", "Veganas", "Torta de chocolate 100% vegana, sin lácteos ni huevos."},
            {"Galletas Veganas de Avena", 8000, 50, "../assets/img/Galletas Veganas de Avena.jpeg", "Veganas", "Galletas crujientes de avena con chips de chocolate vegano."},
            {"Roll de Canela", 15000, 20, "../assets/img/Roll de Canela.jpeg", "Otros", "Suave roll de canela con glaseado de vainilla."},
            {"Empanada de Manzana", 6000, 40, "../assets/img/Empanada de Manzana.jpeg", "Otros", "Empanada dulce rellena de manzana caramelizada."}
        };

        for (Object[] p : productos) {
            Producto producto = new Producto();
            producto.setNombre((String) p[0]);
            producto.setPrecio(BigDecimal.valueOf((Integer) p[1]));
            producto.setStock((Integer) p[2]);
            producto.setImagen((String) p[3]);
            
            // Asignar categoría
            String categoriaNombre = (String) p[4];
            Categoria categoria = categoriasMap.get(categoriaNombre);
            if (categoria != null) {
                producto.setCategoria(categoria);
            }
            
            producto.setDescripcion((String) p[5]);
            productoRepository.save(producto);
        }

        System.out.println("✅ " + productos.length + " productos cargados");
    }
}

