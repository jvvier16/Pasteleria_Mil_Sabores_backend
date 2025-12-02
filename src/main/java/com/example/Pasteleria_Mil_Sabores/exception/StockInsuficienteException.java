package com.example.Pasteleria_Mil_Sabores.exception;

/**
 * Excepción lanzada cuando no hay stock suficiente para completar una compra
 */
public class StockInsuficienteException extends RuntimeException {
    
    private final Long productoId;
    private final String productoNombre;
    private final Integer stockDisponible;
    private final Integer cantidadSolicitada;
    
    public StockInsuficienteException(Long productoId, String productoNombre, 
                                       Integer stockDisponible, Integer cantidadSolicitada) {
        super(String.format("Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d",
            productoNombre, stockDisponible, cantidadSolicitada));
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public String getProductoNombre() {
        return productoNombre;
    }
    
    public Integer getStockDisponible() {
        return stockDisponible;
    }
    
    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}





