package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // POST: Crear/Guardar un producto
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // GET ALL: Obtener todos los productos (con categoría cargada para evitar LazyInitializationException)
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAllWithCategoria();
        return productos;
    }

    // GET BY ID: Obtener un producto por su ID
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findByIdWithCategoria(id).orElse(null);
    }

    // PUT: Actualizar un producto existente
    public Producto actualizarProducto(Producto productoActualizado) {
        Producto existingProducto = productoRepository.findById(productoActualizado.getProductoId()).orElse(null);
        if (existingProducto != null) {
            existingProducto.setNombre(productoActualizado.getNombre());
            existingProducto.setPrecio(productoActualizado.getPrecio());
            existingProducto.setStock(productoActualizado.getStock());
            existingProducto.setImagen(productoActualizado.getImagen());
            existingProducto.setDescripcion(productoActualizado.getDescripcion());
            existingProducto.setCategoria(productoActualizado.getCategoria());
            return productoRepository.save(existingProducto);
        }
        return null;
    }

    // DELETE: Eliminar un producto por su ID
    public String eliminarProducto(Long id) {
        productoRepository.deleteById(id);
        return "Producto con ID " + id + " eliminado.";
    }
}
