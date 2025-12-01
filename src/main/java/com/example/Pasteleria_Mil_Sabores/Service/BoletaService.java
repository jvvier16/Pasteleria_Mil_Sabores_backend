package com.example.Pasteleria_Mil_Sabores.Service;

import com.example.Pasteleria_Mil_Sabores.Entity.Boleta;
import com.example.Pasteleria_Mil_Sabores.Entity.DetalleBoleta;
import com.example.Pasteleria_Mil_Sabores.Entity.Producto;
import com.example.Pasteleria_Mil_Sabores.Entity.Usuario;
import com.example.Pasteleria_Mil_Sabores.Repository.BoletaRepository;
import com.example.Pasteleria_Mil_Sabores.Repository.ProductoRepository;
import com.example.Pasteleria_Mil_Sabores.dto.CrearBoletaRequest;
import com.example.Pasteleria_Mil_Sabores.dto.ItemCarritoDTO;
import com.example.Pasteleria_Mil_Sabores.exception.ProductoNoEncontradoException;
import com.example.Pasteleria_Mil_Sabores.exception.StockInsuficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoletaService {

    // IVA de Chile (19%)
    private static final BigDecimal IVA = new BigDecimal("0.19");

    @Autowired
    private BoletaRepository boletaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Crea una boleta completa a partir de un request de compra.
     * 
     * Este método realiza:
     * 1. Validación de existencia de productos
     * 2. Validación de stock disponible
     * 3. Creación de la boleta con sus detalles
     * 4. Cálculo de subtotales, impuestos y total
     * 5. Descuento de stock de los productos
     * 
     * @param request DTO con los items del carrito
     * @param cliente Usuario autenticado (puede ser null para compras anónimas)
     * @return La boleta creada con todos sus detalles
     * @throws ProductoNoEncontradoException si algún producto no existe
     * @throws StockInsuficienteException si no hay stock suficiente
     */
    @Transactional
    public Boleta procesarCompra(@NonNull CrearBoletaRequest request, Usuario cliente) {
        // Crear la boleta
        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());
        boleta.setEstado("pendiente");
        boleta.setCliente(cliente);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Procesar cada item del carrito
        for (ItemCarritoDTO item : request.getItems()) {
            // 1. Buscar el producto
            Producto producto = productoRepository.findById(item.getProductoId())
                .orElseThrow(() -> new ProductoNoEncontradoException(item.getProductoId()));
            
            // 2. Validar stock
            Integer stockActual = producto.getStock() != null ? producto.getStock() : 0;
            if (stockActual < item.getCantidad()) {
                throw new StockInsuficienteException(
                    producto.getProductoId(),
                    producto.getNombre(),
                    stockActual,
                    item.getCantidad()
                );
            }
            
            // 3. Crear el detalle de boleta
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            
            // 4. Calcular subtotal del item
            BigDecimal precioProducto = producto.getPrecio() != null ? 
                producto.getPrecio() : BigDecimal.ZERO;
            BigDecimal subtotalItem = precioProducto.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotal = subtotal.add(subtotalItem);
            
            // 5. Agregar el detalle a la boleta
            boleta.addItem(detalle);
            
            // 6. Descontar stock
            producto.setStock(stockActual - item.getCantidad());
            productoRepository.save(producto);
        }
        
        // Calcular total con IVA
        BigDecimal impuestos = subtotal.multiply(IVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuestos).setScale(2, RoundingMode.HALF_UP);
        boleta.setTotal(total);
        
        // Guardar la boleta (cascada guarda los detalles también)
        return boletaRepository.save(boleta);
    }

    /**
     * POST: Crear/Guardar una boleta directamente (método legacy)
     * @deprecated Usar procesarCompra() en su lugar para nueva funcionalidad
     */
    @NonNull
    @Transactional
    public Boleta crearBoleta(@NonNull Boleta boleta) {
        // Si la boleta no tiene fecha, asignar la fecha actual
        if (boleta.getFecha() == null) {
            boleta.setFecha(LocalDateTime.now());
        }
        // Si no tiene estado, establecer como pendiente
        if (boleta.getEstado() == null || boleta.getEstado().isEmpty()) {
            boleta.setEstado("pendiente");
        }
        return boletaRepository.save(boleta);
    }
    
    /**
     * GET ALL: Obtener todas las boletas con detalles (para admin)
     */
    @Transactional(readOnly = true)
    public List<Boleta> obtenerTodasLasBoletas() {
        return boletaRepository.findAllWithDetails();
    }

    /**
     * GET BY ID: Obtener una boleta por su ID con todos los detalles
     */
    @Transactional(readOnly = true)
    public Boleta obtenerBoletaPorId(@NonNull Long id) {
        return boletaRepository.findByIdWithDetails(id).orElse(null);
    }
    
    /**
     * GET BY USER: Obtener boletas de un usuario específico con detalles
     */
    @Transactional(readOnly = true)
    public List<Boleta> obtenerBoletasPorUsuario(@NonNull Integer userId) {
        return boletaRepository.findByClienteUserIdWithDetails(userId);
    }
    
    /**
     * GET BY ESTADO: Obtener boletas por estado con detalles
     */
    @Transactional(readOnly = true)
    public List<Boleta> obtenerBoletasPorEstado(String estado) {
        return boletaRepository.findByEstadoWithDetails(estado);
    }
    
    /**
     * PUT: Actualizar estado de una boleta
     */
    @Transactional
    public Boleta actualizarEstadoBoleta(@NonNull Long id, String nuevoEstado) {
        Boleta boleta = boletaRepository.findById(id).orElse(null);
        if (boleta != null) {
            boleta.setEstado(nuevoEstado);
            return boletaRepository.save(boleta);
        }
        return null;
    }
    
    /**
     * Cancelar una boleta y restaurar el stock
     */
    @Transactional
    public Boleta cancelarBoletaConRestauracionStock(@NonNull Long id) {
        Boleta boleta = boletaRepository.findById(id).orElse(null);
        if (boleta == null) {
            return null;
        }
        
        // Solo restaurar stock si la boleta no estaba ya cancelada
        if (!"cancelado".equalsIgnoreCase(boleta.getEstado())) {
            // Restaurar stock de cada producto
            for (DetalleBoleta detalle : boleta.getItems()) {
                Producto producto = detalle.getProducto();
                if (producto != null) {
                    Integer stockActual = producto.getStock() != null ? producto.getStock() : 0;
                    producto.setStock(stockActual + detalle.getCantidad());
                    productoRepository.save(producto);
                }
            }
        }
        
        boleta.setEstado("cancelado");
        return boletaRepository.save(boleta);
    }
    
    /**
     * DELETE: Eliminar una boleta
     */
    @Transactional
    public String eliminarBoleta(@NonNull Long id) {
        if (boletaRepository.existsById(id)) {
            boletaRepository.deleteById(id);
            return "Boleta con ID " + id + " eliminada.";
        }
        return "Boleta no encontrada.";
    }
}
