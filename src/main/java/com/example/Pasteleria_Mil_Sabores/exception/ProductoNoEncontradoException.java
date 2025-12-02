package com.example.Pasteleria_Mil_Sabores.exception;

/**
 * Excepción lanzada cuando no se encuentra un producto
 */
public class ProductoNoEncontradoException extends RuntimeException {
    
    private final Long productoId;
    
    public ProductoNoEncontradoException(Long productoId) {
        super("Producto no encontrado con ID: " + productoId);
        this.productoId = productoId;
    }
    
    public Long getProductoId() {
        return productoId;
    }
}




